package com.apps.brayan.surveyapp

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Handler
import android.util.Log
import com.apps.brayan.surveyapp.coreapp.Cache
import com.apps.brayan.surveyapp.coreapp.NetworkManager
import com.apps.brayan.surveyapp.coreapp.SessionManager
import com.apps.brayan.surveyapp.coreapp.application.MasterApp
import com.apps.brayan.surveyapp.login.LoginCallback
import com.apps.brayan.surveyapp.models.OrgDetail
import com.apps.brayan.surveyapp.models.Survey
import com.apps.brayan.surveyapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.ConnectException
import android.widget.Toast



class SurveyRepository(var firebaseDatabase: FirebaseDatabase, var firebaseAuth: FirebaseAuth, var context:MasterApp) {
    var orgDetailDomain = "https://bdsurvey-4d97c.firebaseio.com/proyectos/{organization}"
    var surveysDomain = "$orgDetailDomain/encuestas"
    var usersDomain = "https://bdsurvey-4d97c.firebaseio.com/usuarios/"


    fun fetchSurveys(liveData:MutableLiveData<ArrayList<Survey>>, organizationName:String,context: Context){
        if(NetworkManager.isNetworkAvailable(context)) {
            val finalUrl = surveysDomain.replace("{organization}", organizationName)
            val myRef = firebaseDatabase.getReferenceFromUrl(finalUrl)
            val surveyListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val listSurveys = ArrayList<Survey>()
                    for (data: DataSnapshot in dataSnapshot.children) {
                        listSurveys.add(Survey(data.child("nombre").value.toString(), data.child("encuesta").value.toString(), data.key))
                    }
                    Cache.generateSurveysCacheByOrganization(context, listSurveys, organizationName)
                    liveData.postValue(listSurveys)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    val cache = Cache.getCacheByOrganization(context, organizationName)
                    if (cache != null)
                        liveData.postValue(cache)
                }
            }
            myRef.addListenerForSingleValueEvent(surveyListener)
        }else{
            val cache = Cache.getCacheByOrganization(context, organizationName)
            if (cache != null)
                liveData.postValue(cache)
        }
    }

    fun fetchOrgDetails(organizationName: String,value:MutableLiveData<OrgDetail>, context: Context){
        Handler().post {
            if (NetworkManager.isNetworkAvailable(context)) {
                val finalUrl = orgDetailDomain.replace("{organization}", organizationName)
                val myRef = firebaseDatabase.getReferenceFromUrl(finalUrl)
                val surveyListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var detail: OrgDetail? = null
                        if (dataSnapshot != null) {
                            detail = dataSnapshot.getValue(OrgDetail::class.java)
                            if (detail != null) {
                                detail.id = organizationName
                                Cache.saveDetailCacheByOrganization(context, detail, organizationName)
                                value.value = detail
                            }
                        }
                        if (detail == null) {
                            value.value = createOrgDetailByString(organizationName)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        val cache = Cache.getCacheOrganizationDetail(context, organizationName)
                        if (cache != null)
                            value.setValue(cache)
                        else
                            value.setValue(createOrgDetailByString(organizationName))
                    }
                }
                myRef.addListenerForSingleValueEvent(surveyListener)
            } else {
                val cache = Cache.getCacheOrganizationDetail(context, organizationName)
                if (cache != null)
                    value.setValue(cache)
                else
                    value.setValue(createOrgDetailByString(organizationName))
            }
        }
    }

    private fun userValidations(userId:String,callback:LoginCallback){
        val finalUrl = usersDomain + userId
        val myRef = firebaseDatabase.getReferenceFromUrl(finalUrl)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        user.id = userId
                        SessionManager.createUser(context, user)
                        callback.onFinishProcess(true)
                    } else {
                        callback.onFinishProcess(false)
                    }
                } else {
                    callback.onFinishProcess(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onFinishProcess(false)
            }
        }
        myRef.addValueEventListener(postListener)

    }

    fun userValidations(email:String, password: String,callback:LoginCallback){
        if(NetworkManager.isNetworkAvailable(context)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("authprocess", "signInWithEmail:success")
                    val user = firebaseAuth.getCurrentUser()
                    if(user!=null){
                        userValidations(user.uid,callback)
                    }else{
                        callback.onFinishProcess(false)
                    }
                } else {
                    callback.onFinishProcess(false)
                }
            }
        }else{
            callback.onFailure(ConnectException())
        }
    }

    fun createOrgDetailByString(organizationName: String): OrgDetail
    {
        val orgDetail = OrgDetail()
        orgDetail.id = organizationName
        orgDetail.nombre = organizationName
        return  orgDetail
    }

}