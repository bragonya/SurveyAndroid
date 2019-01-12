package com.apps.brayan.surveyapp.organizationscreen

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.models.OrgDetail
import com.apps.brayan.surveyapp.models.Survey
import com.apps.brayan.surveyapp.surveychooser.SCClick
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.org_holder.view.*
import kotlinx.android.synthetic.main.survey_chooser_holder.view.*

class OrgHolder (itemView: View?, var listener: OrgClick) : RecyclerView.ViewHolder(itemView) {

    fun bind(position: Int, item: OrgDetail) {
        if(item.logo!=null) {
            Picasso.get().load(item.logo).fit().centerCrop().error(R.drawable.ic_error_black_24dp).into(itemView.imageOrgHolder, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e:Exception) {
                    Log.e("Picasso",e.toString())
                }

            })
        }
        itemView.orgName.text = item.nombre
        itemView.setOnClickListener({ listener.onClick(item.id, itemView.imageOrgHolder,item.logo) })
    }
}