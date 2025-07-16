package com.jws.resume.service

import com.jws.resume.model.Resume

interface FirebaseFunctionsService {

    suspend fun fetchResumeData(code: String): Resume?
}