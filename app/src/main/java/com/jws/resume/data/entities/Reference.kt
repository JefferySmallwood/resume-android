package com.jws.resume.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "references",
    foreignKeys = [ForeignKey(
        entity = ResumeProfile::class,
        parentColumns = ["resume_id"],
        childColumns = ["resume_owner_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["resume_owner_id"])]
)
data class Reference(
    @PrimaryKey(autoGenerate = true)
    val referenceId: Long = 0,
    @ColumnInfo(name = "resume_owner_id")
    val resumeOwnerId: String,
    val name: String,
    val roleWhenWorked: String,
    val description: String? = null,
    val yearsWorkedTogether: String,
    val linkedInProfileUrl: String,
    val profilePictureUrl: String? = null
)