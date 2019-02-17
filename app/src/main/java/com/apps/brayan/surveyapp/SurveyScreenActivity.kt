package com.apps.brayan.surveyapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
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
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_survey_screen.*


class SurveyScreenActivity : AppCompatActivity() {
    lateinit var surveyId:String
    lateinit var organizationId:String
    val domainSurvey = "https://bdsurvey-4d97c.firebaseio.com/proyectos/{organizationId}/respuestas"
    private val PETICION_PERMISO_LOCALIZACION: Int=1023
    private var pendingLambdaTransaction:((hashUser:String, latitude:Double, longitude:Double)->Unit?)?=null
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
            generateRequireData { userHash,longitude,latitude ->
                myRef.child(surveyId).push().setValue(SurveyResponse(System.currentTimeMillis().toString(),fromWeb,userHash,longitude,latitude))
            }


        }
        @JavascriptInterface
        fun back() {
            finish()
        }
    }

    private fun generateRequireData(callback:(hashUser:String, latitude:Double, longitude:Double)->Unit){

        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            callback(getUserHash(), location.latitude, location.longitude)
                        } else {
                            callback(getUserHash(), 0.0, 0.0)
                        }
        }
        }else{
            pendingLambdaTransaction = callback
            ActivityCompat.requestPermissions(this,
                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PETICION_PERMISO_LOCALIZACION);
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingLambdaTransaction != null) {
                    LocationServices.getFusedLocationProviderClient(this).lastLocation
                            .addOnSuccessListener { location: Location? ->

                                if (location != null) {
                                    pendingLambdaTransaction?.invoke(getUserHash(), location.latitude, location.longitude)
                                } else {
                                    pendingLambdaTransaction?.invoke(getUserHash(), 0.0, 0.0)
                                }
                            }
                }
            }else{
                pendingLambdaTransaction?.invoke(getUserHash(), 0.0, 0.0)
            }
        }
    }

    private fun getUserHash():String{
        val loggedUser = SessionManager.getActualUser(applicationContext)
        val idUser: String
        if (loggedUser != null)
            idUser = loggedUser.id
        else
            idUser = "anonymous"
        return idUser
    }
}
