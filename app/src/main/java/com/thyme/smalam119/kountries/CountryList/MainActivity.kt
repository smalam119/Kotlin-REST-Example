package com.thyme.smalam119.kountries.CountryList

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.Model.KountryLocal
import com.thyme.smalam119.kountries.Network.ApiService
import com.thyme.smalam119.kountries.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var recyclerVIew: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: KountryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViews()
        makeGetAllCountryNetworkCall()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        prepareSearchView(menu)
        return true
    }

    fun makeGetAllCountryNetworkCall() {
        progressBar!!.visibility = View.VISIBLE
        var apiService = ApiService.create()
        apiService.getAllKountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    progressBar!!.visibility = View.GONE
                    prepareAdapter(result)
                }, { error ->
                    progressBar!!.visibility = View.GONE
                    error.printStackTrace()
                })
    }

    fun prepareViews() {
        prepareToolBar()
        prepareRecyclerView()
        prepareProgressBar()
    }

    fun prepareToolBar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //supportActionBar.apply { title = "Kountries" }
    }

    fun prepareRecyclerView() {
        recyclerVIew = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerVIew!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    fun prepareSearchView(menu: Menu?) {
        var menuItem = menu!!.findItem(R.id.search)
        var searchView = menuItem.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.queryHint = "enter country name"
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                Log.d("MainActivity", query)
                adapter!!.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                Log.d("MainActivity", query)
                adapter!!.getFilter().filter(query)
                return false
            }
        })
    }

    fun prepareProgressBar() {
        progressBar = findViewById(R.id.progressBar)
    }

    fun prepareAdapter(kountryList: ArrayList<Kountry>) {
        adapter = KountryListAdapter(kountryList)
        recyclerVIew!!.adapter = adapter
    }

    fun getMockData(): ArrayList<KountryLocal> {
        val countryList = ArrayList<KountryLocal>()
        countryList.add(KountryLocal("Bangladesh", "Republic Of Bangladesh", "Dhaka"))
        countryList.add(KountryLocal("Bangladesh", "Republic Of Bangladesh", "Dhaka"))
        countryList.add(KountryLocal("Bangladesh", "Republic Of Bangladesh", "Dhaka"))
        return countryList
    }
}
