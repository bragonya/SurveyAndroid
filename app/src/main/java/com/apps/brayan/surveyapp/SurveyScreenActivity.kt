package com.apps.brayan.surveyapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.Toast
import com.apps.brayan.surveyapp.coreapp.SessionManager
import com.apps.brayan.surveyapp.coreapp.SurveyConstants
import com.apps.brayan.surveyapp.coreapp.SurveyManagerFile
import com.apps.brayan.surveyapp.models.SurveyResponse
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_survey_screen.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.*
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.webkit.WebChromeClient
import android.webkit.ValueCallback
import android.webkit.WebView
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class SurveyScreenActivity : AppCompatActivity() {
    lateinit var surveyId:String
    lateinit var organizationId:String
    private var mFilePathCallback: ValueCallback<Array<Uri>>? =null
    private val INPUT_FILE_REQUEST_CODE: Int = 1121
    val domainSurvey = "https://bdsurvey-4d97c.firebaseio.com/proyectos/{organizationId}/respuestas"
    private val PETICION_PERMISO_LOCALIZACION: Int=1023
    private var pendingLambdaTransaction:((hashUser:String, latitude:Double, longitude:Double)->Unit?)?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_survey_screen)
        val surveyContent:String = intent.getStringExtra(SurveyConstants.SURVEY_BODY_INTENT)
        if (!hasPermissions(this@SurveyScreenActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this@SurveyScreenActivity, arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE);
        }
        surveyId = intent.getStringExtra(SurveyConstants.SURVEY_ID_INTENT)
        organizationId = intent.getStringExtra(SurveyConstants.KEY_ORG)
        SurveyManagerFile.setupSurveyToFile(surveyContent,this)
        SurveyManagerFile.readFile(this)
        setupWebView(webViewSurvey)
    }

    private var mCameraPhotoPath: String? = null

    private val PERMISSION_CODE: Int = 1122

    fun setupWebView(webview: WebView){

        webview.settings.javaScriptEnabled = true

        val activity = this
        with(webview) {
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    activity.setProgress(progress * 1000)
                }


                override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                    // Double check that we don't have any existing callbacks
                    if (mFilePathCallback != null) {
                        mFilePathCallback!!.onReceiveValue(null)
                    }
                    mFilePathCallback = filePath


                    var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent!!.resolveActivity(packageManager) != null) {
                        // Create the File where the photo should go
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile()
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            Log.e("surveyscreen", "Unable to create Image File", ex)
                        }

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath()
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile))
                        } else {
                            takePictureIntent = null
                        }
                    }
                    val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                    contentSelectionIntent.type = "image/*"
                    val intentArray: Array<Intent>
                    if (takePictureIntent != null) {
                        intentArray = arrayOf(takePictureIntent)
                    } else {
                        intentArray = arrayOf();
                    }
                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                    return true
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


    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int,data:Intent?) {

        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results:Uri?  = null
        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null || data.dataString == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {

                    results = Uri.parse(mCameraPhotoPath);
                }
            } else {
                val dataString: String = data.getDataString()
                if (dataString != null) {
                    results = Uri.parse(dataString);
                }
            }
        }
        if(results!=null)
            mFilePathCallback?.onReceiveValue(arrayOf(results))
        else
            mFilePathCallback?.onReceiveValue(null)
        mFilePathCallback = null;
        return;
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun sendData(fromWeb: String) {
            Log.d("myFinalSurvey",fromWeb)
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
