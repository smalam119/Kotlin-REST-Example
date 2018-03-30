package com.thyme.smalam119.kountries.Modules.KountryDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.thyme.smalam119.kountries.Utils.AppConstants
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.Utils.Network.ApiClient
import com.thyme.smalam119.kountries.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class KountryDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    var mapFragment: SupportMapFragment? = null
    var capitalTV: TextView? = null
    var areaTV: TextView? = null
    var populationTV: TextView? = null
    var regionTV: TextView? = null
    var timeZoneTV: TextView? = null
    var languageTV: TextView? = null
    var currencyTV: TextView? = null
    var imageView: ImageView? = null
    var nameTV: TextView? = null
    var globalLatLng: LatLng? = null
    var shimmerViewContainer: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kountry_detail)

        // getting the 2 word country alpha code from main activity
        var alphaCode = intent.getStringExtra(AppConstants.ALPHA_2_CODE_EXTRA)

        prepareView()

        // make get country network call
        makeGetCountryNetworkCall(alphaCode)
    }

    fun prepareMap() {
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    // place marker on the country geo graphic location
    fun placeMarker(googleMap: GoogleMap, latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,4f))
        googleMap.addMarker(markerOptions)
    }

    fun makeGetCountryNetworkCall(alphaCode: String) {
        shimmerViewContainer!!.startShimmerAnimation()
        var apiService = ApiClient.create()
        apiService.getKountry(alphaCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    shimmerViewContainer!!.stopShimmerAnimation()
                    bindData(result)
                }, { error ->
                    shimmerViewContainer!!.stopShimmerAnimation()
                    error.printStackTrace()
                })
    }

    fun prepareView() {
        imageView = findViewById(R.id.flag_image_view_detail)
        nameTV = findViewById(R.id.name_text_view_detail)
        capitalTV = findViewById(R.id.capital_text_view_detail)
        areaTV = findViewById(R.id.area_text_view_detail)
        populationTV = findViewById(R.id.population_text_view_detail)
        regionTV = findViewById(R.id.region_text_view_detail)
        timeZoneTV = findViewById(R.id.time_zone_text_view_detail)
        languageTV = findViewById(R.id.language_text_view_detail)
        currencyTV = findViewById(R.id.currency_text_view_detail)
        shimmerViewContainer = findViewById(R.id.shimmer_view_container)
    }

    fun bindData(kountry: Kountry) {
        var latLng = LatLng(kountry.latlng[0],kountry.latlng[1])
        globalLatLng  = latLng
        imageView = findViewById(R.id.flag_image_view_detail)
        Picasso
                .with(this)
                .load(AppConstants.BASE_URL_FLAG + kountry.alpha2Code + ".png")
                .into(imageView)

        nameTV!!.text = kountry.name
        capitalTV!!.text = kountry.capital
        areaTV!!.text = kountry.area.toString() + " km^2"
        populationTV!!.text = kountry.population.toString()
        regionTV!!.text = kountry.region + ", " + kountry.subregion
        timeZoneTV!!.text = kountry.timezones[0]
        languageTV!!.text = kountry.languages[0].name
        currencyTV!!.text = kountry.currencies[0].name + " " + "(" + kountry.currencies[0].symbol + ")"

        Picasso
                .with(this)
                .load(AppConstants.BASE_URL_FLAG + kountry.alpha2Code + ".png")
                .into(imageView)

        prepareMap()
    }

    override fun onMapReady(p0: GoogleMap?) {
        // place the marker when map is ready
        placeMarker(p0!!,globalLatLng!!)
    }
}
