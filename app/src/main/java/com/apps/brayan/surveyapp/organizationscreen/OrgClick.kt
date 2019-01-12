package com.apps.brayan.surveyapp.organizationscreen

import android.view.View

interface OrgClick {
    fun onClick(orgName:String, imgView:View, img:String?)
}