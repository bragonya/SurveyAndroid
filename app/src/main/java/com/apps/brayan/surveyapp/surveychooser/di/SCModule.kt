package com.apps.brayan.surveyapp.surveychooser.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.api.NetworkLayerModule
import com.apps.brayan.surveyapp.firebase.database.FirebaseModule
import com.apps.brayan.surveyapp.surveychooser.SCViewModel
import com.apps.brayan.surveyapp.viewmodel.SurveyViewModelFactory
import com.apps.brayan.surveyapp.viewmodel.ViewModelKey
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = arrayOf(NetworkLayerModule::class))
abstract class SCModule{
    @Binds
    internal abstract fun bindViewModelFactory(factory: SurveyViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SCViewModel::class)
    internal abstract fun postListViewModel(viewModel: SCViewModel): ViewModel
}