package com.apps.brayan.surveyapp.models

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class SurveyResponse(date: String, body: String, var userHash: String = "Anonymous",var latitude:Double=0.0,var longitude:Double=0.0) {

    var date: String?= date
    var body: HashMap<*,*>?=null

    init {
        this.body = ObjectMapper().readValue(body, HashMap::class.java)
    }

}