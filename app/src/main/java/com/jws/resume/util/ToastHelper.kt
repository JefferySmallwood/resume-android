package com.jws.resume.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ToastHelper {

    private var isToastShowing = false
    private var currentToastJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())


    fun showUniqueToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        if (!isToastShowing) {
            isToastShowing = true
            Toast.makeText(context.applicationContext, text, duration).show()

            currentToastJob?.cancel()
            currentToastJob = scope.launch {
                delay(timeMillis = duration.toToastMillis() + 500L)
                isToastShowing = false
            }
        }
    }

    fun showToast(context: Context, text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context.applicationContext, text, duration).show()
    }

    private fun Int.toToastMillis(): Long {
        return if (this == Toast.LENGTH_LONG) 3500L else 2000L
    }

    // Call this if the app is destroyed and you want to ensure the scope is cancelled
    fun cancelScope() {
        scope.cancel()
    }
}