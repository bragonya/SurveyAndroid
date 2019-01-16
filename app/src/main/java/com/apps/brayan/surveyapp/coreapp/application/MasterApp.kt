package com.apps.brayan.surveyapp.coreapp.application

import android.app.Application
import com.apps.brayan.surveyapp.coreapp.application.di.AppComponent
import com.apps.brayan.surveyapp.coreapp.application.di.AppModule
import com.apps.brayan.surveyapp.coreapp.application.di.DaggerAppComponent

class MasterApp: Application() {
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