import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import {DocumentData} from 'firebase-admin/firestore';

admin.initializeApp();
const db = admin.firestore();

interface RequestPayload {
    code: string;
    userId: string;
}

interface AccessCodeData {
    code: string;
    isActive: boolean;
    usesRemaining: number | undefined;
    expirationDate: admin.firestore.Timestamp | undefined;
    associatedUserID: string;
    firstUseTimestamp: admin.firestore.Timestamp | undefined;
}

exports.getResumeData = functions.https.onCall(async (requestData) => {
    const {code: accessCode} = requestData.data as RequestPayload;

    if (!accessCode) {
        throw new functions.https.HttpsError('invalid-argument', 'Access code is required.');
    }

    try {
        // 1. Validate the access code
        const accessCodeRef = db.collection('accessCodes').where('code', '==', accessCode);
        const accessCodeSnapshot = await accessCodeRef.get();

        if (accessCodeSnapshot.empty) {
            throw new functions.https.HttpsError('not-found', 'Invalid access code.');
        }
        const codeDoc = accessCodeSnapshot.docs[0];
        const codeData = codeDoc.data() as AccessCodeData;

        if (!codeData.isActive) {
            throw new functions.https.HttpsError('permission-denied', 'Access code is inactive.');
        }

        if (codeData.usesRemaining !== undefined && codeData.usesRemaining <= 0) {
            throw new functions.https.HttpsError('permission-denied', 'Access code has no uses remaining.');
        }
        if (
            codeData.expirationDate &&
            codeData.expirationDate instanceof admin.firestore.Timestamp &&
            codeData.expirationDate.toDate() < new Date()
        ) {
            throw new functions.https.HttpsError('permission-denied', 'Access code has expired.');
        }

        // 2. Decrement usesRemaining and set firstUseTimestamp if applicable
        const updates: { [key: string]: any } = {};
        if (codeData.usesRemaining !== undefined) {
            updates.usesRemaining = admin.firestore.FieldValue.increment(-1);
        }
        if (!codeData.firstUseTimestamp) {
            updates.firstUseTimestamp = admin.firestore.FieldValue.serverTimestamp();
        }

        if (Object.keys(updates).length > 0) {
            await codeDoc.ref.update(updates);
        }

        // 3. Fetch the real resume data
        const resumeDataRef = db.collection('users').doc(codeData.associatedUserID);
        const resumeDoc = await resumeDataRef.get();
        const data = resumeDoc.data();

        if (!resumeDoc.exists || !data) {
            throw new functions.https.HttpsError('not-found', 'Resume data not found for this user.');
        }

        return {
            data: data as DocumentData,
            resume: data['resume'],
        };
    } catch (error: any) {
        console.error('Error fetching resume data:', error);
        if (error instanceof functions.https.HttpsError) {
            throw error;
        }
        throw new functions.https.HttpsError('internal', 'An unexpected error occurred.', error.message);
    }
});
