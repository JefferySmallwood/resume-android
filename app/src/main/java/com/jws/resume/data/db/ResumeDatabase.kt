package com.jws.resume.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jws.resume.data.dao.MetaDataDao
import com.jws.resume.data.dao.ResumeDao
import com.jws.resume.data.entities.EducationEntry
import com.jws.resume.data.entities.JobExperience
import com.jws.resume.data.entities.MetaData
import com.jws.resume.data.entities.Reference
import com.jws.resume.data.entities.ResumeProfile
import com.jws.resume.data.entities.Skill
import com.jws.resume.util.Converters

@Database(
    entities = [
        ResumeProfile::class,
        JobExperience::class,
        Skill::class,
        EducationEntry::class,
        Reference::class,
        MetaData::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resumeDao(): ResumeDao

    abstract fun metaDataDao(): MetaDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "resume_database"
                )
                    // Use with caution, for development only.
                    // .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}