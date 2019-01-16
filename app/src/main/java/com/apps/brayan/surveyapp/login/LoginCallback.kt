package com.apps.brayan.surveyapp.login

interface LoginCallback {
    fun onFinishProcess(isLogged:Boolean)
    fun onFailure(reason: Throwable)
}