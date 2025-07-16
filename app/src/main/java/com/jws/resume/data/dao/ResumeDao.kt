package com.jws.resume.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jws.resume.data.entities.EducationEntry
import com.jws.resume.data.entities.JobExperience
import com.jws.resume.data.entities.Reference
import com.jws.resume.data.entities.ResumeProfile
import com.jws.resume.data.entities.Skill
import com.jws.resume.model.Resume
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResumeProfile(resume: ResumeProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobExperiences(experiences: List<JobExperience>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<Skill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducationEntries(educationEntries: List<EducationEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferences(references: List<Reference>)

    @Transaction
    suspend fun insertResume(resume: Resume) {
        insertResumeProfile(resume.resume)
        // TODO: Revisit, it's likely that with the changes on the backend, the resumeOwnerId is already populated
        val parentId = resume.resume.resumeId
        insertJobExperiences(resume.experiences.map { it.copy(resumeOwnerId = parentId) })
        insertSkills(resume.skills.map { it.copy(resumeOwnerId = parentId) })
        insertEducationEntries(resume.educationEntries.map { it.copy(resumeOwnerId = parentId) })
        insertReferences(resume.references.map { it.copy(resumeOwnerId = parentId) })
    }

    @Update
    suspend fun updateResumeProfile(resume: ResumeProfile)

    @Query("DELETE FROM resumes WHERE resume_id = :resumeId")
    suspend fun deleteResumeById(resumeId: String)

    @Query("DELETE FROM resumes")
    suspend fun deleteAllResumes() // Use with caution!

    @Transaction
    @Query("SELECT * FROM resumes WHERE resume_id = :resumeId")
    fun getResumeById(resumeId: String): Flow<Resume?>

    @Transaction
    @Query("SELECT * FROM resumes ORDER BY resume_id DESC")
    fun getAllResumes(): Flow<List<Resume>>

    @Query("SELECT * FROM resumes ORDER BY resume_id DESC")
    fun getAllResumeProfiles(): Flow<List<ResumeProfile>>
}