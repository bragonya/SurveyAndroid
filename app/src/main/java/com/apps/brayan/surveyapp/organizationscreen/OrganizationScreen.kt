package com.apps.brayan.surveyapp.organizationscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.apps.brayan.surveyapp.LoginActivity
import com.apps.brayan.surveyapp.R
import com.apps.brayan.surveyapp.coreApp.SessionManager
import com.apps.brayan.surveyapp.coreApp.SurveyConstants
import com.apps.brayan.surveyapp.surveychooser.SCViewModel
import com.apps.brayan.surveyapp.surveychooser.SurveyChooser
import kotlinx.android.synthetic.main.activity_organization_screen.*
import kotlinx.android.synthetic.main.app_bar_organization_screen.*
import kotlinx.android.synthetic.main.content_organization_screen.*
import kotlinx.android.synthetic.main.nav_header_organization_screen.*
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager


class OrganizationScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OrgClick {

    lateinit var adapter: OrgAdapter
    lateinit var model:OrgViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_screen)
        setSupportActionBar(toolbar)

        model = ViewModelProviders.of(this).get(OrgViewModel::class.java)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setupRecyclerView()
    }

    fun setupRecyclerView(){
        recyclerOrganization.layoutManager = LinearLayoutManager(this)
        var arrayOrgs:ArrayList<String> = ArrayList()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.organization_screen, menu)
        textNameNavigation.text = SessionManager.getActualUser(this)?.nombre
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
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
        val intent = Intent(this,SurveyChooser::class.java)
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
