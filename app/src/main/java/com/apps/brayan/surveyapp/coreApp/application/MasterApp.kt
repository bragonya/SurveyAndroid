package com.apps.brayan.surveyapp.coreApp.application

import android.app.Application
import com.apps.brayan.surveyapp.coreApp.application.di.AppComponent
import com.apps.brayan.surveyapp.coreApp.application.di.AppModule
import com.apps.brayan.surveyapp.coreApp.application.di.DaggerAppComponent

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