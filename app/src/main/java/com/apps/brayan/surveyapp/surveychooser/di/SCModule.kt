package com.apps.brayan.surveyapp.surveychooser.di

import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.api.NetworkLayerModule
import com.apps.brayan.surveyapp.firebase.database.FirebaseModule
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(NetworkLayerModule::class))
class SCModule{
    @Provides
    fun provideRepository(firebaseDatabase: FirebaseDatabase): SurveyRepository {
        return SurveyRepository(firebaseDatabase)
    }
}