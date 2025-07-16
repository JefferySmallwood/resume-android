package com.jws.resume.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "skills",
    foreignKeys = [ForeignKey(
        entity = ResumeProfile::class,
        parentColumns = ["resume_id"],
        childColumns = ["resume_owner_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["resume_owner_id"])]
)
data class Skill(
    @PrimaryKey(autoGenerate = true)
    val skillId: Long = 0,
    @ColumnInfo(name = "resume_owner_id")
    val resumeOwnerId: String,
    val category: String,
    val name: String
)