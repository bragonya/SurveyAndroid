package com.apps.brayan.surveyapp.coreApp.application.di

import com.apps.brayan.surveyapp.coreApp.application.MasterApp
import com.apps.brayan.surveyapp.viewmodel.ViewModelComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: MasterApp)
    fun getViewModelComponent(): ViewModelComponent
}