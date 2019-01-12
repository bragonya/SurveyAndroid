package com.apps.brayan.surveyapp

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Handler
import com.apps.brayan.surveyapp.coreApp.Cache
import com.apps.brayan.surveyapp.coreApp.NetworkManager
import com.apps.brayan.surveyapp.models.OrgDetail
import com.apps.brayan.surveyapp.models.Survey
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class SurveyRepository() {
    var orgDetailDomain = "https://bdsurvey-4d97c.firebaseio.com/proyectos/{organization}"
    var surveysDomain = orgDetailDomain + "/encuestas"


    fun fetchSurveys(liveData:MutableLiveData<ArrayList<Survey>>, organizationName:String,context: Context){
        if(NetworkManager.isNetworkAvailable(context)) {
            val finalUrl = surveysDomain.replace("{organization}", organizationName)
            val myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(finalUrl)
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
                val myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(finalUrl)
                val surveyListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var detail: OrgDetail? = null
                        if (dataSnapshot != null) {
                            detail = dataSnapshot.getValue(OrgDetail::class.java)
                            if (detail != null) {
                                detail.id = organizationName
                                Cache.saveDetailCacheByOrganization(context, detail, organizationName)
                                value.setValue(detail)
                            }
                        }
                        if (detail == null) {
                            value.setValue(createOrgDetailByString(organizationName))
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

    fun createOrgDetailByString(organizationName: String): OrgDetail
    {
        val orgDetail = OrgDetail()
        orgDetail.id = organizationName
        orgDetail.nombre = organizationName
        return  orgDetail
    }

}