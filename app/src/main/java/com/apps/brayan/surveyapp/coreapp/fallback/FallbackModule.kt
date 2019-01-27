package com.apps.brayan.surveyapp.coreapp.fallback

import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import com.apps.brayan.surveyapp.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FallbackModule {
    @Provides
    fun getFallbackManager():FallbackManager{
        return FallbackManager()
    }
}