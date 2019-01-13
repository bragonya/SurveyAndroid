package com.apps.brayan.surveyapp.coreApp.application.di

import com.apps.brayan.surveyapp.coreApp.application.MasterApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: MasterApp) {
    @Provides
    @Singleton
    fun provideApp() = app
}