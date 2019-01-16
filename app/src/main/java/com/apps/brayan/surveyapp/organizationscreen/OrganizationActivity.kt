package com.apps.brayan.surveyapp.organizationscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.apps.brayan.surveyapp.login.LoginActivity
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.coreApp.SessionManager
import com.apps.brayan.surveyapp.coreApp.SurveyConstants
import com.apps.brayan.surveyapp.surveychooser.SurveyChooserActivity
import kotlinx.android.synthetic.main.activity_organization_screen.*
import kotlinx.android.synthetic.main.app_bar_organization_screen.*
import kotlinx.android.synthetic.main.content_organization_screen.*
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import com.apps.brayan.surveyapp.coreApp.application.MasterApp
import javax.inject.Inject


class OrganizationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OrgClick {
    val component by lazy { (application as MasterApp).component.getViewModelComponent() }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var adapter: OrgAdapter
    lateinit var model:OrgViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_screen)
        setSupportActionBar(toolbar)
        component.inject(this)
        model = ViewModelProviders.of(this,viewModelFactory).get(OrgViewModel::class.java)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setupRecyclerView()
    }

    fun setupRecyclerView(){
        recyclerOrganization.layoutManager = LinearLayoutManager(this)
        val arrayOrgs:ArrayList<String> = ArrayList()
        SessionManager.getActualUser(this)?.orgaizaciones?.keys?.toCollection(arrayOrgs)
        adapter = OrgAdapter(ArrayList(),this,this)
        recyclerOrganization.adapter = adapter
        setupAdapter(arrayOrgs)

    }

    fun setupAdapter(organizations: ArrayList<String>){
        if(organizations!=null) {
            model.getDetail().observe(this,Observer{
                adapter.addLast(it!!)
                adapter.notifyItemInserted(adapter.itemCount-1)
            })
            model.getDetailOrganizations(organizations,this)
        }else{
            // error
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.resources_item_nav -> {
                // Handle the camera action
            }
            R.id.account_item_nav -> {
                SessionManager.logOut(this)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(orgName: String, imgView:View, img:String?) {
        val intent = Intent(this,SurveyChooserActivity::class.java)
        intent.putExtra(SurveyConstants.KEY_ORG,orgName)
        intent.putExtra(SurveyConstants.IMG_ORG,img)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imgView, "imgTransition")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent,options.toBundle())
        }else{
            startActivity(intent)
        }
    }


}
