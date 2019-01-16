package com.apps.brayan.surveyapp.models

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class SurveyResponse {

    var date: String?=null
    var body: HashMap<*,*>?=null

    constructor(date: String, body: String) {

        this.body = ObjectMapper().readValue(body, HashMap::class.java)
        this.date = date
    }

}