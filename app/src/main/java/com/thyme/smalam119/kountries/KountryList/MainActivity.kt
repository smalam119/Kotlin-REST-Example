package com.thyme.smalam119.kountries.KountryList

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.app.SearchManager
import android.content.Context
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.thyme.smalam119.kountries.About.AboutFragment
import com.thyme.smalam119.kountries.R


class MainActivity : AppCompatActivity(), KountryListFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
    }

    var toolbar: Toolbar? = null
    var kountryListFragment: KountryListFragment? = null
    var bottomNavigationView: BottomNavigationView? = null
    var searchView: SearchView? = null

    // bottom navigation item selection
    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.home -> {
                    kountryListFragment = KountryListFragment.Companion.newInstance("","")
                    addFragment(kountryListFragment!!)
                    toolbar!!.visibility = View.VISIBLE
                    return true
                }
                R.id.favorite -> {
                    val fragment = AboutFragment.Companion.newInstance("","")
                    addFragment(fragment)
                    toolbar!!.visibility = View.INVISIBLE
                    return true
                }
            }
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareAllView()
        // add kountry list fragment as default fragment
        addFragment(kountryListFragment!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        prepareSearchView(menu)
        return true
    }

    fun prepareAllView() {
        prepareToolBar()
        prepareBottomNavView()
        kountryListFragment = KountryListFragment.Companion.newInstance("","")
    }

    fun prepareToolBar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    fun prepareBottomNavView() {
        bottomNavigationView = findViewById(R.id.navigation_bottom)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun prepareSearchView(menu: Menu?) {
        var menuItem = menu!!.findItem(R.id.search)

        //getting search view reference from menu item
        searchView = menuItem.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView!!.queryHint = "enter country name"
        searchView!!.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        //query text listener for search view
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                kountryListFragment!!.adapter!!.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                Log.d("tag","mListAdapter="+kountryListFragment!!.adapter!!)
                kountryListFragment!!.adapter!!.getFilter().filter(query)
                return false
            }
        })
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
                .commit()
    }

}
