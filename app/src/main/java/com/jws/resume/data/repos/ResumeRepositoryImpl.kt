package com.jws.resume.data.repos

import android.util.Log
import com.jws.resume.data.dao.ResumeDao
import com.jws.resume.model.Resume
import com.jws.resume.service.FirebaseFunctionsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ResumeRepositoryImpl(
    private val resumeDao: ResumeDao,
    private val functionsService: FirebaseFunctionsService
) : ResumeRepository {

    override fun getAllResumes(): Flow<List<Resume>> {
        return resumeDao.getAllResumes()
    }

    override fun getResumeById(resumeId: String): Flow<Resume?> {
        return resumeDao.getResumeById(resumeId)
    }

    override suspend fun insertResume(resume: Resume) {
        resumeDao.insertResume(resume)
    }

    override suspend fun updateResume(resume: Resume) {
        resumeDao.insertResume(resume)
    }

    override suspend fun deleteResumeById(resumeId: String) {
        resumeDao.deleteResumeById(resumeId)
    }

    override suspend fun deleteAllResumes() {
        resumeDao.deleteAllResumes()
    }

    override suspend fun fetchAndStoreResumeFromApi(accessCode: String): Resume? {
        return withContext(Dispatchers.IO) {
            try {
                val apiResume: Resume? = functionsService.fetchResumeData(accessCode)

                if (apiResume != null) {
                    resumeDao.insertResume(apiResume)
                    Log.i("ResumeRepository", "Successfully fetched and stored resume ID: ${apiResume.resume.resumeId}")
                    return@withContext apiResume
                } else {
                    Log.w("ResumeRepository", "API returned null for access code: $accessCode")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("ResumeRepository", "Error fetching or storing resume from API for code $accessCode", e)
                throw e
            }
        }
    }
}
