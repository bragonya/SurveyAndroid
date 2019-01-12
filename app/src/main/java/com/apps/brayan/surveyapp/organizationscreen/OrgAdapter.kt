package com.apps.brayan.surveyapp.organizationscreen

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.models.OrgDetail

class OrgAdapter(val items : ArrayList<OrgDetail>, val context: Context, var listener: OrgClick): RecyclerView.Adapter<OrgHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrgHolder {
        return OrgHolder(LayoutInflater.from(context).inflate(R.layout.org_holder, parent, false),listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun replaceItems(list: ArrayList<OrgDetail>){
        items.clear()
        items.addAll(list)
    }

    fun addLast(item:OrgDetail){
        items.add(item)
    }

    override fun onBindViewHolder(holder: OrgHolder, position: Int) {
        holder.bind(position,items.get(position))
    }
}