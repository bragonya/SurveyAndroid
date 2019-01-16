package com.apps.brayan.surveyapp.coreapp

class JsonSurveyGenerator{
    companion object {
        fun generatevalidJson(validJson:String):String{
            return "var json = $validJson;"
        }
    }
}