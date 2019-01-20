package com.apps.brayan.surveyapp.coreapp.application.di

import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import com.apps.brayan.surveyapp.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(val app: MasterApp) {
    @Provides
    @Singleton
    fun provideApp() = app
}