package com.apps.brayan.surveyapp.organizationscreen

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.apps.brayan.surveyapp.SurveyRepository
import com.apps.brayan.surveyapp.models.OrgDetail
import javax.inject.Inject

class OrgViewModel @Inject constructor(var repository: SurveyRepository): ViewModel() {
    private var detail: MutableLiveData<OrgDetail>
    init {
        detail = MutableLiveData()
    }

    fun getDetail(): LiveData<OrgDetail> {
        return detail
    }

    fun getDetailOrganizations(names: ArrayList<String>, context: Context)
    {
        names.map { fetchOrganizationDetail(it, context) }
    }

    private fun fetchOrganizationDetail(organizationName:String, context: Context){
        repository.fetchOrgDetails(organizationName,detail,context)
    }
}