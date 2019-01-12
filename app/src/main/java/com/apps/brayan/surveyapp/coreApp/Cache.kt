package com.apps.brayan.surveyapp.coreApp

import android.content.Context
import android.preference.PreferenceManager
import com.apps.brayan.surveyapp.models.OrgDetail
import com.apps.brayan.surveyapp.models.Survey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Cache {

    companion object {
        val prefixDetaiil = "detail_"
        fun generateSurveysCacheByOrganization(context: Context, list:ArrayList<Survey>, keyOrg:String){
            val gson = Gson()
            val surveyString = gson.toJson(list)
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(keyOrg,surveyString).apply()
        }

        fun getCacheByOrganization(context: Context, keyOrg:String):ArrayList<Survey>?{
            val cache = PreferenceManager.getDefaultSharedPreferences(context).getString(keyOrg,"")
            var cacheInstance:ArrayList<Survey>? =null
            if(!cache.isEmpty()) {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<Survey>>() {}.type
                cacheInstance = gson.fromJson<ArrayList<Survey>>(cache, type)
            }
            return cacheInstance
        }

        fun saveDetailCacheByOrganization(context: Context, detail: OrgDetail, keyOrg:String){
            val gson = Gson()
            val detailString = gson.toJson(detail)
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(prefixDetaiil+keyOrg,detailString).apply()
        }

        fun getCacheOrganizationDetail(context: Context, keyOrg:String):OrgDetail?{
            val cache = PreferenceManager.getDefaultSharedPreferences(context).getString(prefixDetaiil+keyOrg,"")
            var detail:OrgDetail? =null
            if(!cache.isEmpty()) {
                val gson = Gson()
                detail = gson.fromJson<OrgDetail>(cache,OrgDetail::class.java)
            }
            return detail
        }
    }
}