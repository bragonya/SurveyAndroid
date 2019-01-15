package com.apps.brayan.surveyapp.surveychooser

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.SurveyScreen
import com.apps.brayan.surveyapp.api.NetworkLayerModule
import com.apps.brayan.surveyapp.coreApp.SurveyConstants
import com.apps.brayan.surveyapp.coreApp.application.MasterApp
import com.apps.brayan.surveyapp.coreApp.fallback.FallbackCase
import com.apps.brayan.surveyapp.coreApp.fallback.FallbackManager
import com.apps.brayan.surveyapp.firebase.database.FirebaseModule
import com.apps.brayan.surveyapp.models.Survey
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_survey_chooser.*
import javax.inject.Inject

class SurveyChooser : AppCompatActivity(), SCClick {
    val component by lazy { (application as MasterApp).component.getViewModelComponent() }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var model:SCViewModel
    lateinit var adapter: SCAdapter
    lateinit var fallbackManager: FallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_chooser)
        component.inject(this)
        collapsing_toolbar.post { collapsing_toolbar.requestLayout() }
        fallbackManager = FallbackManager()
        val organizationName:String = intent.getStringExtra(SurveyConstants.KEY_ORG) ?: ""
        setupHeader(intent.getStringExtra(SurveyConstants.IMG_ORG))
        model = ViewModelProviders.of(this,viewModelFactory).get(SCViewModel::class.java)
        setupRecyclerView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        fetchData(intent.getStringExtra(SurveyConstants.KEY_ORG) ?: "")
    }

    fun setupHeader(img:String?){
        if(img!=null){
            Picasso.get().load(img).fit().centerCrop().error(R.drawable.ic_error_black_24dp).into(imgHeader)
        }
    }

    fun setupRecyclerView(){
        recyclerSurveyChooser.layoutManager = LinearLayoutManager(this)
        adapter =  SCAdapter(ArrayList(), this,this)
        recyclerSurveyChooser.adapter = adapter
    }

    fun fetchData(organizationName:String){
        model.getSurveys().observe(this, Observer {
            showNewDataSet(it)
        })
        model.fetchSurveysByOrganization(organizationName,this)
    }

    fun showNewDataSet(list: ArrayList<Survey>?){
        if(list!=null){
            if(list.size == 0){
                showEmptyError()
                return
            }
            adapter.replaceItems(list)
            adapter.notifyDataSetChanged()
        }else{
            showGenericError()
        }
    }

    fun detachFallback(){
        fallbackManager.detachCurrentFallback()
    }

    fun showGenericError(){
        fallbackManager.injectFallback(FallbackCase.GENERIC_ERROR,getFallbackContainter())
    }

    fun showEmptyError(){
        fallbackManager.injectFallback(FallbackCase.EMPTY_ERROR,getFallbackContainter())
    }

    fun getFallbackContainter():ConstraintLayout{
        return  recyclerContainer
    }

    override fun onClick(item: Survey) {
        val intent = Intent(this,SurveyScreen::class.java)
        intent.putExtra(SurveyConstants.SURVEY_BODY_INTENT,item.body)
        intent.putExtra(SurveyConstants.SURVEY_ID_INTENT,item.id)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            supportFinishAfterTransition()
        super.onBackPressed()
    }
}
