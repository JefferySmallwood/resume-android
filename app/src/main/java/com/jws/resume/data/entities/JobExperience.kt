package com.jws.resume.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "job_experiences",
    foreignKeys = [ForeignKey(
        entity = ResumeProfile::class,
        parentColumns = ["resume_id"],
        childColumns = ["resume_owner_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["resume_owner_id"])]
)
data class JobExperience(
    @PrimaryKey(autoGenerate = true)
    val jobExperienceId: Long = 0,
    @ColumnInfo(name = "resume_owner_id")
    val resumeOwnerId: String,
    val company: String,
    val title: String,
    val startDate: String,
    val endDate: String?,
    val responsibilities: List<String>,
    val technologies: List<String>
)