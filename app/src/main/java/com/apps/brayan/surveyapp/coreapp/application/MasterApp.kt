package com.apps.brayan.surveyapp.coreapp.application

import android.app.Activity
import android.app.Application
import com.apps.brayan.surveyapp.coreapp.application.di.AppComponent
import com.apps.brayan.surveyapp.coreapp.application.di.AppModule
import com.apps.brayan.surveyapp.coreapp.application.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MasterApp: Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }


}