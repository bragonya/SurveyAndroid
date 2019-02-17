package com.apps.brayan.surveyapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.apps.brayan.surveyapp.coreapp.SessionManager
import com.apps.brayan.surveyapp.coreapp.SurveyConstants
import com.apps.brayan.surveyapp.coreapp.SurveyManagerFile
import com.apps.brayan.surveyapp.models.SurveyResponse
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_survey_screen.*


class SurveyScreenActivity : AppCompatActivity() {
    lateinit var surveyId:String
    lateinit var organizationId:String
    val domainSurvey = "https://bdsurvey-4d97c.firebaseio.com/proyectos/{organizationId}/respuestas"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_survey_screen)
        val surveyContent:String = intent.getStringExtra(SurveyConstants.SURVEY_BODY_INTENT)
        surveyId = intent.getStringExtra(SurveyConstants.SURVEY_ID_INTENT)
        organizationId = intent.getStringExtra(SurveyConstants.KEY_ORG)
        SurveyManagerFile.setupSurveyToFile(surveyContent,this)
        SurveyManagerFile.readFile(this)
        setupWebView(webViewSurvey)
    }


    fun setupWebView(webview: WebView){

        webview.settings.javaScriptEnabled = true

        val activity = this
        with(webview) {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    activity.setProgress(progress * 1000)
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    Toast.makeText(activity, "Oh no! $description", Toast.LENGTH_SHORT).show()
                }
            }
            addJavascriptInterface(JavaScriptInterface(),"JSInterface")

            loadUrl(SurveyConstants.SURVEY_DOMAIN+SurveyConstants.SURVEY_MAIN)
        }
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun sendData(fromWeb: String) {
            val myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(domainSurvey.replace("{organizationId}",organizationId))
            val logguedUser = SessionManager.getActualUser(applicationContext)
            if(logguedUser!=null)
                myRef.child(surveyId).push().setValue(SurveyResponse(System.currentTimeMillis().toString(),fromWeb,logguedUser.id))
            else
                myRef.child(surveyId).push().setValue(SurveyResponse(System.currentTimeMillis().toString(),fromWeb))
        }
        @JavascriptInterface
        fun back() {
            finish()
        }
    }

}
