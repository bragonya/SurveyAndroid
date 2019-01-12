package com.apps.brayan.surveyapp.surveychooser

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.models.Survey

class SCAdapter(val items : ArrayList<Survey>, val context: Context, var listener:SCClick): RecyclerView.Adapter<SCHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SCHolder {
        return SCHolder(LayoutInflater.from(context).inflate(R.layout.survey_chooser_holder, parent, false),listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun replaceItems(list: ArrayList<Survey>){
        items.clear()
        items.addAll(list)
    }

    override fun onBindViewHolder(holder: SCHolder, position: Int) {
        holder.bind(position,items.get(position))
    }
}