package com.jws.resume.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jws.resume.data.entities.MetaData
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(metaData: MetaData)

    @Query("SELECT * FROM meta_data WHERE id = :id")
    fun getMetaData(id: Int = MetaData.DEFAULT_ID): Flow<MetaData?>

    @Query("SELECT * FROM meta_data WHERE id = :id")
    suspend fun getMetaDataOnce(id: Int = MetaData.DEFAULT_ID): MetaData?

    @Query("SELECT current_resume_id FROM meta_data WHERE id = :id")
    fun getCurrentResumeId(id: Int = MetaData.DEFAULT_ID): Flow<String?>

    @Query("UPDATE meta_data SET current_resume_id = :resumeId WHERE id = :id")
    suspend fun updateCurrentResumeId(resumeId: String?, id: Int = MetaData.DEFAULT_ID)

    @Query("UPDATE meta_data SET current_resume_id = NULL WHERE id = :id")
    suspend fun clearCurrentResumeId(id: Int = MetaData.DEFAULT_ID)
}