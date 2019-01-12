package com.apps.brayan.surveyapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.apps.brayan.surveyapp.coreApp.NetworkManager
import com.apps.brayan.surveyapp.coreApp.SessionManager
import com.apps.brayan.surveyapp.models.User
import com.apps.brayan.surveyapp.organizationscreen.OrganizationScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var usersDomain = "https://bdsurvey-4d97c.firebaseio.com/usuarios/"
    var firebaseInstance:FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupFirebaseDatabase()
        initialFlow()
    }

    fun initialFlow(){
        val actualUser = SessionManager.getActualUser(this)
        if(actualUser != null){
            if(NetworkManager.isNetworkAvailable(this)) {
                updateData(actualUser.id)
            }else{
                goToOrganizationScreen()
            }
        }else{
            logo.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }
    }

    fun updateData(userId:String){
        val finalUrl = usersDomain+userId
        val myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(finalUrl)
        myRef.keepSynced(true)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot!=null){
                    val user = dataSnapshot.getValue(User::class.java)
                    if(user!=null){
                        user.id = userId
                        SessionManager.createUser(applicationContext,user)
                        goToOrganizationScreen()
                    }else{
                        goToOrganizationScreen()
                    }
                }else{
                   goToOrganizationScreen()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                goToOrganizationScreen()
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

    fun goToOrganizationScreen(){
        val intent = Intent(applicationContext, OrganizationScreen::class.java)
        startActivity(intent)
        finish()
    }

    fun setupFirebaseDatabase(){

        if (firebaseInstance==null) {
            firebaseInstance = FirebaseDatabase.getInstance()
            firebaseInstance?.setPersistenceEnabled(true)
        }
    }
}
