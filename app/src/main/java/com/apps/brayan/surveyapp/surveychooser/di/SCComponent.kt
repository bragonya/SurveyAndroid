package com.apps.brayan.surveyapp.surveychooser.di

import com.apps.brayan.surveyapp.surveychooser.SurveyChooser
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(SCModule::class))
interface SCComponent {
    fun inject(activity: SurveyChooser)
}