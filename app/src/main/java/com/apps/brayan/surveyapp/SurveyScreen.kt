package com.apps.brayan.surveyapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.view.Window
import android.webkit.JavascriptInterface
import com.apps.brayan.surveyapp.coreApp.JsonSurveyGenerator
import com.apps.brayan.surveyapp.coreApp.SurveyConstants
import com.apps.brayan.surveyapp.coreApp.SurveyManagerFile
import com.apps.brayan.surveyapp.models.SurveyResponse
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_survey_screen.*


class SurveyScreen : AppCompatActivity() {
    lateinit var surveyId:String
    val domainSurvey = "https://bdsurvey-4d97c.firebaseio.com/proyectos/organizacionheifer/respuestas"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_survey_screen)
        val surveyContent:String = intent.getStringExtra(SurveyConstants.SURVEY_BODY_INTENT)
        surveyId = intent.getStringExtra(SurveyConstants.SURVEY_ID_INTENT)
        SurveyManagerFile.setupSurveyToFile(surveyContent,this)
        SurveyManagerFile.readFile(this)
        setupWebView(webViewSurvey)
    }


    fun setupWebView(webview: WebView){

        webview.getSettings().setJavaScriptEnabled(true)

        val activity = this
        with(webview) {
            setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                activity.setProgress(progress * 1000)
            }
        })
            setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, "Oh no! $description", Toast.LENGTH_SHORT).show()
            }
        })
            addJavascriptInterface(JavaScriptInterface(),"JSInterface")

            loadUrl(SurveyConstants.SURVEY_DOMAIN+SurveyConstants.SURVEY_MAIN)
        }
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun sendData(fromWeb: String) {
            val myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(domainSurvey)
            myRef.child(surveyId).push().setValue(SurveyResponse(System.currentTimeMillis().toString(),fromWeb))
        }

        @JavascriptInterface
        fun back() {
            finish()
        }
    }

}
