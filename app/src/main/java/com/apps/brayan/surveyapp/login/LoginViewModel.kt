package com.apps.brayan.surveyapp.login

import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.apps.brayan.surveyapp.SurveyRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(val repository: SurveyRepository): ViewModel() {

    fun attemptLogin(emailStr:String,passwordStr:String, callback: AttempToLoginCallback) {
        if (passwordStr.isNullOrEmpty() || !isPasswordValid(passwordStr)) {
            callback.onErrorPassword()
        }else if (TextUtils.isEmpty(emailStr)) {
            callback.onErrorId()
        } else {
            callback.showProgress(true)
            repository.userValidations(emailStr,passwordStr,object : LoginCallback{
                override fun onFinishProcess(isLogged: Boolean) {
                    callback.showProgress(false)
                    callback.onFinishAttempt(isLogged)

                }

                override fun onFailure(reason: Throwable) {
                    callback.showProgress(false)
                    callback.onInternetFailure()
                }
            })
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}