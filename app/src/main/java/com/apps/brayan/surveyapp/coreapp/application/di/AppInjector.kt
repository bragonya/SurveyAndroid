package com.apps.brayan.surveyapp.coreapp.application.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import dagger.android.AndroidInjection

object AppInjector {
    fun init(app: MasterApp) {
        DaggerAppComponent.builder().appModule(AppModule(app))
                .build().inject(app)
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        handleActivity(activity)
                    }

                    override fun onActivityStarted(activity: Activity) {

                    }

                    override fun onActivityResumed(activity: Activity) {

                    }

                    override fun onActivityPaused(activity: Activity) {

                    }

                    override fun onActivityStopped(activity: Activity) {

                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

                    }

                    override fun onActivityDestroyed(activity: Activity) {

                    }
                })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is InjectedActivity) {
            AndroidInjection.inject(activity)
        }
    }
}