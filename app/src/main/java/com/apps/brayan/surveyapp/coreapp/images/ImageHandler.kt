package com.apps.brayan.surveyapp.coreapp.images

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ImageHandler {

    companion object Image {
        private val PREFS_FILENAME = "com.survey.images.handler"
        private val IMAGES_NOT_DISPATCHED = "IMAGES_NOT_DISPATCHED"
        fun saveEvent(path: Uri,context: Context){
            addNewUriToDispatch(path,context)
        }

        fun dispatchEvents(context:Context){
            Executors.newSingleThreadExecutor().execute{
                Log.d("BDImage","init dispatching process")
                val list = getListImagesNotDispatched(context)
                list.forEach {
                    uploadFile(Uri.parse(it.value),it.key, context)
                }
            }

        }

        fun addHashKeys(names: ArrayList<String>,uris:ArrayList<String>, context: Context){
            addNewUriToDispatch(names,uris,context)
        }

        private fun getClearName(name:Uri,context: Context):String{
            //val antiSlashing = name.split(".")
            var outputString:String = getRealName(name,context)
            val arr = outputString.split(".")
            if(arr.size > 1){
                outputString = arr[0]+"_"+System.currentTimeMillis()+"."+arr[1]
            }
            Log.d("pathNewName",outputString)
            return outputString
        }

        private fun getRealName(name:Uri,context: Context):String {
            if (name.toString().startsWith("content://")) {
                var cursor: Cursor? = null;
                try {
                    cursor = context.getContentResolver().query(name, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }else{
                        return name.lastPathSegment
                    }
                } catch (e: Exception){
                    return name.lastPathSegment
                }finally {
                    cursor?.close();
                }
            } else {
                return name.lastPathSegment
            }

        }

        private fun getListImagesNotDispatched(context: Context): HashMap<String,String>{
            val  prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
            val type = object : TypeToken<HashMap<String,String>>() {

            }.getType()
            val listString = prefs.getString(IMAGES_NOT_DISPATCHED,"")
            if(listString.isNullOrEmpty()){
                return HashMap()
            }
            return Gson().fromJson(listString, type)
        }

        private fun saveListImagesNotDispatched(list:HashMap<String,String>,context: Context){
            val json = Gson().toJson(list)
            val editor = context.getSharedPreferences(PREFS_FILENAME, 0).edit()
            editor.putString(IMAGES_NOT_DISPATCHED, json)
            .apply()
        }

        private fun addNewUriToDispatch(path:Uri, context: Context){
            val arr = getListImagesNotDispatched(context)
            arr.put(getClearName(path,context),path.toString())
            saveListImagesNotDispatched(arr,context)
        }

        private fun addNewUriToDispatch(names: ArrayList<String>,uris:ArrayList<String>, context: Context){
            if(names.size == uris.size) {
                val arr = getListImagesNotDispatched(context)
                names.forEachIndexed { index,name  ->
                    Log.d("BDImage","add: "+name)
                    arr.put(name,uris[index])
                }
                saveListImagesNotDispatched(arr, context)
            }
        }

        private fun deleteEvent(key:String,context: Context){
            val list = getListImagesNotDispatched(context)
            Log.d("BDImage","delete: "+key)
            list.remove(key)
            saveListImagesNotDispatched(list,context)
        }

        private fun uploadFile(filePath:Uri,name:String, context: Context){
            val storageRef = FirebaseStorage.getInstance("gs://bdsurvey-4d97c.appspot.com")
            val imagesRef = storageRef.reference.child("/SurveyImages/${name}")

            imagesRef.putFile(filePath).addOnSuccessListener {
                deleteEvent(name,context)
            }
        }
    }
}