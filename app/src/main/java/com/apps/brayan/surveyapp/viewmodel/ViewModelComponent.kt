package com.apps.brayan.surveyapp.viewmodel

import com.apps.brayan.surveyapp.organizationscreen.OrganizationScreen
import com.apps.brayan.surveyapp.surveychooser.SurveyChooser
import com.apps.brayan.surveyapp.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ViewModelModule::class))
interface ViewModelComponent {
    fun inject(activity: SurveyChooser)
    fun inject(activity: OrganizationScreen)
}