package com.jws.resume.service

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.jws.resume.model.Resume
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
class FirebaseFunctionsServiceImpl(private val functions: FirebaseFunctions): FirebaseFunctionsService {

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun fetchResumeData(code: String): Resume? {
        val data = hashMapOf(
            "code" to code
        )

        try {
            val result = functions
                .getHttpsCallable("getResumeData")
                .call(data)
                .await()

            val resultData = result.data

            if (resultData is Map<*, *>) {
                try {
                    val resumeJson = resultData["resume"] as? String ?: ""

                    val resume = jsonParser.decodeFromString<Resume>(resumeJson)

                    return resume
                } catch (e: Exception) {
                    Log.e("FirebaseFunctionsService", "Error parsing result data into Resume object", e)
                    return null
                }
            } else {
                Log.w("FirebaseFunctionsService", "Unexpected data type from function: ${resultData?.javaClass?.name}")
                return null
            }

        } catch (e: FirebaseFunctionsException) {
            Log.e("FirebaseFunctionsService", "FirebaseFunctionsException calling function. Code: ${e.code}, Message: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            Log.e("FirebaseFunctionsService", "Generic exception calling function", e)
            throw e
        }
    }
}

