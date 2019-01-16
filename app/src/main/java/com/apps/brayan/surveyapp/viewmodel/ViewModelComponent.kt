package com.apps.brayan.surveyapp.viewmodel

import com.apps.brayan.surveyapp.login.LoginActivity
import com.apps.brayan.surveyapp.organizationscreen.OrganizationActivity
import com.apps.brayan.surveyapp.surveychooser.SurveyChooserActivity
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(activity: SurveyChooserActivity)
    fun inject(activity: OrganizationActivity)
    fun inject(activity: LoginActivity)
}