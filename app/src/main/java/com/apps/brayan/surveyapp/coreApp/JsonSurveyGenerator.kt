package com.apps.brayan.surveyapp.coreApp

class JsonSurveyGenerator{
    companion object {
        fun generatevalidJson(validJson:String):String{
            return "var json = "+validJson+";"
        }
    }
}