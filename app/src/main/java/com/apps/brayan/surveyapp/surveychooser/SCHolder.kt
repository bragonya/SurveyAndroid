package com.apps.brayan.surveyapp.surveychooser

import android.support.v7.widget.RecyclerView
import android.view.View
import com.apps.brayan.surveyapp.models.Survey
import kotlinx.android.synthetic.main.survey_chooser_holder.view.*

class SCHolder(itemView: View?, var listener:SCClick) : RecyclerView.ViewHolder(itemView) {

    fun bind(position: Int, item: Survey) {
        itemView.surveyName.text = item.name
        itemView.setOnClickListener({ listener.onClick(item) })
    }
}