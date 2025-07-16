package com.jws.resume

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
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
