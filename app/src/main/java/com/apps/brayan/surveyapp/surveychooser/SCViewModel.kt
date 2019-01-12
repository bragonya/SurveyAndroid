package com.apps.brayan.surveyapp.surveychooser

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.models.Survey

class SCViewModel : ViewModel() {
    private var repository:SurveyRepository
    private var surveyList:MutableLiveData<ArrayList<Survey>>
    init {
        repository = SurveyRepository()
        surveyList = MutableLiveData()
    }

    fun getSurveys():LiveData<ArrayList<Survey>>{
        return surveyList
    }

    fun fetchSurveysByOrganization(organizationName:String, context: Context){
        repository.fetchSurveys(surveyList,organizationName,context)
    }


}