package com.apps.brayan.surveyapp.models

import com.apps.brayan.surveyapp.SplashActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject


@IgnoreExtraProperties
class SurveyResponse {

    var date: String?=null
    var body: HashMap<*,*>?=null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(date: String, body: String) {

        this.body = ObjectMapper().readValue(body, HashMap::class.java)
        this.date = date
    }

}