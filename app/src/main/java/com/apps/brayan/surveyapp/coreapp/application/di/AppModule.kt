package com.apps.brayan.surveyapp.coreapp.application.di

import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: MasterApp) {
    @Provides
    @Singleton
    fun provideApp() = app
}