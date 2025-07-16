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
    usesRemaining: number | null;
    expirationDate: admin.firestore.Timestamp | string | null;
    associatedUserID: string;
    firstUseTimestamp: admin.firestore.Timestamp | null;
}

interface Resume {
    resume: ResumeProfile;
    experiences: JobExperience[];
    skills: Skill[];
    educationEntries: EducationEntry[];
    references: Reference[];
}

interface HomeInfo{
    tagline: string;
    location: string;
    yearsExperience: number;
    email: string;
    phoneNumber: string;
    linkedInUrl: string;
    githubUrl: string;
    profilePictureUrl?: string;
}

interface ResumeProfile {
    resumeId: string;
    homeInfo: HomeInfo;
}

interface JobExperience {
    resumeOwnerId: string;
    company: string;
    title: string;
    startDate: string;
    endDate?: string;
    responsibilities: string[];
    technologies: string[];
}

interface Skill {
    resumeOwnerId: string;
    category: string;
    name: string;
}

interface EducationEntry {
    resumeOwnerId: string;
    institution: string;
    degree: string;
    yearsAttended: string;
    description: string;
    imageUrl?: string;
    contentDescription?: string;
}

interface Reference {
    resumeOwnerId: string;
    name: string;
    roleWhenWorked: string;
    description?: string;
    yearsWorkedTogether: string;
    linkedInProfileUrl: string;
    profilePictureUrl?: string;
}

function convertDataToResume(data: DocumentData, resumeId: string): Resume {
    const homeInfo: HomeInfo = data['homeInfo'] as HomeInfo;

    const resume: ResumeProfile = {
        resumeId: resumeId,
        homeInfo,
    };
    const experiences: JobExperience[] = (data['experience'])
        .map((elem: JobExperience) => ({
            ...elem,
            resumeOwnerId: resumeId,
        } as JobExperience));
    const skills: Skill[] = (data['skills'])
        .map((elem: Skill) => ({
            ...elem,
            resumeOwnerId: resumeId,
        } as Skill));
    const educationEntries: EducationEntry[] = (data['education'])
        .map((elem: EducationEntry) => ({
            ...elem,
            resumeOwnerId: resumeId,
        } as EducationEntry));
    const references: Reference[] = (data['references'])
        .map((elem: Reference) => ({
            ...elem,
            resumeOwnerId: resumeId,
        } as Reference));

    return {
        resume,
        experiences,
        skills,
        educationEntries,
        references,
    };
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
        if (codeData.usesRemaining !== null && codeData.usesRemaining <= 0) {
            throw new functions.https.HttpsError('permission-denied', 'Access code has no uses remaining.');
        }
        if (
            codeData.expirationDate &&
            codeData.expirationDate !== 'null' &&
            codeData.expirationDate !== null &&
            codeData.expirationDate instanceof admin.firestore.Timestamp &&
            codeData.expirationDate.toDate() < new Date()
        ) {
            throw new functions.https.HttpsError('permission-denied', 'Access code has expired.');
        }

        // 2. Decrement usesRemaining and set firstUseTimestamp if applicable
        const updates: { [key: string]: any } = {};
        if (codeData.usesRemaining !== null) {
            updates.usesRemaining = admin.firestore.FieldValue.increment(-1);
        }
        if (!codeData.firstUseTimestamp) {
            updates.firstUseTimestamp = admin.firestore.FieldValue.serverTimestamp();
        }
        await codeDoc.ref.update(updates);

        // 3. Fetch the real resume data
        const resumeDataRef = db.collection('users').doc(codeData.associatedUserID);
        const resumeDoc = await resumeDataRef.get();
        const data = resumeDoc.data();

        if (!resumeDoc.exists || !data) {
            throw new functions.https.HttpsError('not-found', 'Resume data not found for this user.');
        }

        const resume = convertDataToResume(data, codeData.associatedUserID);

        return {
            data: data as DocumentData,
            resume: JSON.stringify(resume),
        };
    } catch (error: any) {
        console.error('Error fetching resume data:', error);
        if (error instanceof functions.https.HttpsError) {
            throw error;
        }
        throw new functions.https.HttpsError('internal', 'An unexpected error occurred.', error.message);
    }
});
