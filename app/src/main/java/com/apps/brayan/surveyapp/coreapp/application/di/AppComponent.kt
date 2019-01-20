package com.apps.brayan.surveyapp.coreapp.application.di

import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import com.apps.brayan.surveyapp.viewmodel.ViewModelComponent
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,AppModule::class,BindingActivitiesModule::class])
interface AppComponent {
    fun inject(app: MasterApp)
    //fun getViewModelComponent(): ViewModelComponent
}