package com.apps.brayan.surveyapp.coreApp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class SurveyManagerFile{
    companion object {
        fun setupSurveyToFile(body: String,context:Context){
            try {
                val fos = context.openFileOutput(SurveyConstants.SURVEY_BODY_FILE, Context.MODE_PRIVATE)
                val string:String = JsonSurveyGenerator.generatevalidJson(body)
                fos.write(string.toByteArray())
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun readFile(context: Context){

            val text = StringBuffer()
            try {
                val bReader = BufferedReader(InputStreamReader(context.openFileInput(SurveyConstants.SURVEY_BODY_FILE)))
                var line: String? =bReader.readLine()
                while (line != null) {
                    text.append(line + "\n")
                    line = bReader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.d("filesfe",text.toString())

        }
    }
}