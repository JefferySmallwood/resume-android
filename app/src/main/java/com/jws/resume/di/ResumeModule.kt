package com.jws.resume.di

import android.content.Context
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.jws.resume.data.dao.MetaDataDao
import com.jws.resume.data.dao.ResumeDao
import com.jws.resume.data.db.AppDatabase
import com.jws.resume.data.repos.ResumeRepository
import com.jws.resume.data.repos.ResumeRepositoryImpl
import com.jws.resume.service.FirebaseFunctionsService
import com.jws.resume.service.FirebaseFunctionsServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideResumeDao(appDatabase: AppDatabase): ResumeDao = appDatabase.resumeDao()

    @Provides
    @Singleton
    fun provideMetaDataDao(appDatabase: AppDatabase): MetaDataDao = appDatabase.metaDataDao()

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions = Firebase.functions

    @Provides
    @Singleton
    fun provideFirebaseFunctionsService(firebaseFunctions: FirebaseFunctions): FirebaseFunctionsService {
        return FirebaseFunctionsServiceImpl(firebaseFunctions)
    }

    @Provides
    @Singleton
    fun provideResumeRepository(
        resumeDao: ResumeDao,
        metaDataDao: MetaDataDao,
        firebaseFunctionsService: FirebaseFunctionsService
    ): ResumeRepository {
        return ResumeRepositoryImpl(resumeDao, metaDataDao, firebaseFunctionsService)
    }
}