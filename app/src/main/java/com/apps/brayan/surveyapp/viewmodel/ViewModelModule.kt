package com.apps.brayan.surveyapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.api.NetworkLayerModule
import com.apps.brayan.surveyapp.firebase.database.FirebaseModule
import com.apps.brayan.surveyapp.organizationscreen.OrgViewModel
import com.apps.brayan.surveyapp.surveychooser.SCViewModel
import com.apps.brayan.surveyapp.viewmodel.SurveyViewModelFactory
import com.apps.brayan.surveyapp.viewmodel.ViewModelKey
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = arrayOf(NetworkLayerModule::class))
abstract class ViewModelModule{
    @Binds
    internal abstract fun bindViewModelFactory(factory: SurveyViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SCViewModel::class)
    internal abstract fun scViewModel(viewModel: SCViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrgViewModel::class)
    internal abstract fun orgViewModel(orgViewModel: OrgViewModel): ViewModel
}