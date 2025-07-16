package com.jws.resume.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jws.resume.data.entities.EducationEntry
import com.jws.resume.data.entities.JobExperience
import com.jws.resume.data.entities.Reference
import com.jws.resume.data.entities.ResumeProfile
import com.jws.resume.data.entities.Skill
import kotlinx.serialization.Serializable

@Serializable
data class Resume(
    @Embedded
    val resume: ResumeProfile,

    @Relation(
        parentColumn = "resume_id",
        entityColumn = "resume_owner_id"
    )
    val experiences: List<JobExperience>,

    @Relation(
        parentColumn = "resume_id",
        entityColumn = "resume_owner_id"
    )
    val skills: List<Skill>,

    @Relation(
        parentColumn = "resume_id",
        entityColumn = "resume_owner_id"
    )
    val educationEntries: List<EducationEntry>,

    @Relation(
        parentColumn = "resume_id",
        entityColumn = "resume_owner_id"
    )
    val references: List<Reference>
)
