package com.apps.brayan.surveyapp.coreapp.application

import android.app.Activity
import android.app.Application
import com.apps.brayan.surveyapp.coreapp.application.di.AppComponent
import com.apps.brayan.surveyapp.coreapp.application.di.AppInjector
import com.apps.brayan.surveyapp.coreapp.application.di.AppModule
import com.apps.brayan.surveyapp.coreapp.application.di.DaggerAppComponent
import com.google.firebase.database.FirebaseDatabase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MasterApp: Application(), HasActivityInjector {
    var firebaseInstance:FirebaseDatabase?=null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector


    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)

        setupFirebaseDatabase()
    }
    fun setupFirebaseDatabase(){

        if (firebaseInstance==null) {
            firebaseInstance = FirebaseDatabase.getInstance()
            firebaseInstance?.setPersistenceEnabled(true)
        }
    }

}