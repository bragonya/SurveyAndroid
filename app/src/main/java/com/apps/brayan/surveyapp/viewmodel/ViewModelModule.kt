package com.apps.brayan.surveyapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.apps.brayan.surveyapp.api.NetworkLayerModule
import com.apps.brayan.surveyapp.login.LoginViewModel
import com.apps.brayan.surveyapp.organizationscreen.OrgViewModel
import com.apps.brayan.surveyapp.surveychooser.SCViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [NetworkLayerModule::class])
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

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel
}