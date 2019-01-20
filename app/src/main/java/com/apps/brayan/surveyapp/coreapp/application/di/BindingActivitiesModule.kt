package com.apps.brayan.surveyapp.coreapp.application.di

import com.apps.brayan.surveyapp.login.LoginActivity
import com.apps.brayan.surveyapp.organizationscreen.OrganizationActivity
import com.apps.brayan.surveyapp.surveychooser.SurveyChooserActivity
import com.apps.brayan.surveyapp.viewmodel.ViewModelModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingActivitiesModule {
    @ContributesAndroidInjector()
    internal abstract fun bindSurveyChooserActivity(): SurveyChooserActivity

    @ContributesAndroidInjector()
    internal abstract fun bindOrganizationActivity(): OrganizationActivity

    @ContributesAndroidInjector()
    internal abstract fun bindLoginActivity(): LoginActivity
}
