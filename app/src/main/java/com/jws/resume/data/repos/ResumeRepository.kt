package com.jws.resume.data.repos

import com.jws.resume.model.Resume
import kotlinx.coroutines.flow.Flow

interface ResumeRepository {

    suspend fun initializeMetaDataIfNeeded()

    fun getCurrentResumeId(): Flow<String?>

    suspend fun setCurrentResumeId(resumeId: String)

    suspend fun clearCurrentResumeId()

    fun getAllResumes(): Flow<List<Resume>>

    fun getResumeById(resumeId: String): Flow<Resume?>

    suspend fun insertResume(resume: Resume)

    suspend fun updateResume(resume: Resume)

    suspend fun deleteResumeById(resumeId: String)

    suspend fun deleteAllResumes()

    suspend fun fetchAndStoreResumeFromApi(accessCode: String): Resume?
}
