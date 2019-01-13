package com.apps.brayan.surveyapp.firebase.database

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule() {

    @Provides
    @Singleton
    fun provideFirebaseInstance() = FirebaseDatabase.getInstance()
}