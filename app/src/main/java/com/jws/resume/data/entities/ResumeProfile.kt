package com.jws.resume.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = "resumes")
data class ResumeProfile(
    // TODO: Setup and use FB document ID's instead of auto-generated IDs
    @PrimaryKey
    @ColumnInfo(name = "resume_id")
    val resumeId: String = UUID.randomUUID().toString(),
    @Embedded val homeInfo: HomeInfo,
)

@Serializable
data class HomeInfo(
    val name: String,
    val tagline: String,
    val location: String,
    val yearsExperience: Int,
    val email: String,
    val phoneNumber: String,
    val linkedInUrl: String,
    val githubUrl: String,
    val profilePictureUrl: String? = null
)