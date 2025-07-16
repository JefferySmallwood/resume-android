package com.jws.resume.data.repos

import com.jws.resume.model.Resume
import kotlinx.coroutines.flow.Flow

interface ResumeRepository {

    fun getAllResumes(): Flow<List<Resume>>

    fun getResumeById(resumeId: String): Flow<Resume?>

    suspend fun insertResume(resume: Resume)

    suspend fun updateResume(resume: Resume)

    suspend fun deleteResumeById(resumeId: String)

    suspend fun deleteAllResumes()

    suspend fun fetchAndStoreResumeFromApi(accessCode: String): Resume?
}
