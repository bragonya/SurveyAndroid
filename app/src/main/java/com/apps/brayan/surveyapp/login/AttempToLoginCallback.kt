package com.apps.brayan.surveyapp.login

interface AttempToLoginCallback {
    fun onInternetFailure()
    fun showProgress(isNeededProgress:Boolean)
    fun onErrorPassword()
    fun onErrorId()
    fun onFinishAttempt(isLogged:Boolean)
}