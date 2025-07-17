package com.jws.resume.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "meta_data")
data class MetaData(
    @PrimaryKey
    val id: Int = DEFAULT_ID,

    @ColumnInfo(name = "current_resume_id")
    val currentResumeId: String? = null,
) {
    companion object {
        const val DEFAULT_ID = 42
    }
}