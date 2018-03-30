package com.thyme.smalam119.kountries.Modules.KountryList

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
import com.thyme.smalam119.kountries.Modules.Random.RandomFragment
import com.thyme.smalam119.kountries.R

class MainActivity : AppCompatActivity(), KountryListFragment.OnFragmentInteractionListener,
        RandomFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
    }

    var toolbar: Toolbar? = null
    var kountryListFragment: KountryListFragment? = null
    var aboutFragment: RandomFragment? = null
    var bottomNavigationView: BottomNavigationView? = null
    var searchView: SearchView? = null

    // bottom navigation item selection
    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.home -> {
                    addFragment(kountryListFragment!!,aboutFragment!!)
                    toolbar!!.visibility = View.VISIBLE
                    return true
                }
                R.id.favorite -> {
                    addFragment(aboutFragment!!,kountryListFragment!!)
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
        addFragment(kountryListFragment!!,aboutFragment!!)
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
        aboutFragment = RandomFragment.Companion.newInstance("","")
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

    private fun addFragment(fragment: Fragment,fragment2: Fragment) {
        if (fragment.isAdded) {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .hide(fragment2)
                    .show(fragment)
                    .commit()
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .hide(fragment2)
                    .add(R.id.content, fragment, fragment.javaClass.getSimpleName())
                    .show(fragment)
                    .commit()
        }

    }

}
