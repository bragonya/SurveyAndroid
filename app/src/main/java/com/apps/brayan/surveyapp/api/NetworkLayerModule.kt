package com.apps.brayan.surveyapp.api

import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.firebase.database.FirebaseModule
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
@Module(includes = arrayOf(FirebaseModule::class))
class NetworkLayerModule{
    @Provides
    fun provideRepository(firebaseDatabase: FirebaseDatabase): SurveyRepository{
        return SurveyRepository(firebaseDatabase)
    }
}