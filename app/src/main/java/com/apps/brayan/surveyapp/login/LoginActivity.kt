package com.apps.brayan.surveyapp.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import com.apps.brayan.surveyapp.organizationscreen.OrganizationActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    val component by lazy { (application as MasterApp).component.getViewModelComponent() }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var model:LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_start_scene)
        component.inject(this)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                model.attemptLogin(email.text.toString(),password.text.toString(), AttempToLoginImpl())
                return@OnEditorActionListener true
            }
            false
        })
        model = ViewModelProviders.of(this,viewModelFactory).get(LoginViewModel::class.java)
        sign_in_button.setOnClickListener { model.attemptLogin(email.text.toString(),password.text.toString(), AttempToLoginImpl()) }
        Handler().postDelayed({updateConstraints(R.layout.activity_login)},1000)
    }

    private fun notifyPasswordError(){
        password.error = getString(R.string.error_invalid_password)
        password.requestFocus()
    }

    private fun notifyIdError(){
        email.error = getString(R.string.error_field_required)
        email.requestFocus()
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showLoader(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    fun finalStep(success: Boolean?){

        if (success!!) {
            val intent = Intent(this,OrganizationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            password.error = getString(R.string.error_incorrect_password)
            password.requestFocus()
        }
    }

    inner class AttempToLoginImpl: AttempToLoginCallback{
        override fun onInternetFailure() {
            Toast.makeText(applicationContext,getString(R.string.error_internet_connection), Toast.LENGTH_SHORT).show()
        }

        override fun showProgress(isNeededProgress: Boolean) {
            showLoader(isNeededProgress)
        }

        override fun onErrorPassword() {
            notifyPasswordError()
        }

        override fun onErrorId() {
            notifyIdError()
        }

        override fun onFinishAttempt(isLogged: Boolean) {
            finalStep(isLogged)
        }

    }



    fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(root)
        TransitionManager.beginDelayedTransition(root)
    }
}
