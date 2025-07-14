package com.jws.resume

import android.app.Application
import android.util.Log
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.jws.resume.model.Resume
import com.jws.resume.model.mapFirestoreDataToResume
import kotlinx.coroutines.tasks.await

class ResumeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Connect to emulators if in debug mode
//        if (BuildConfig.DEBUG) {
//            Firebase.functions.useEmulator("127.0.0.1", 5001) // 10.0.2.2 for Android emulator loopback
//            Firebase.firestore.useEmulator("127.0.0.1", 8080)
//        }
    }
}

// TODO: Move to ViewModel or Repository class
suspend fun fetchResumeData(accessCode: String): Resume? {
    val functions = Firebase.functions
    val data = hashMapOf("code" to accessCode)

    return try {
        val result = functions
            .getHttpsCallable("getResumeData")
            .call(data)
            .await()
        Log.d("ResumeApp", "Fetched resume data: ${result.data}")
        val resumeData = result.data as? Map<String, Any>
        val resume = mapFirestoreDataToResume(resumeData)
        Log.d("ResumeApp", "Mapped resume data: $resume")


        resume
    } catch (e: Exception) {
        Log.e("ResumeApp", "Error fetching resume:", e)
        null
    }
}
