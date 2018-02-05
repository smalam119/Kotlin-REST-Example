package com.thyme.smalam119.kountries.Random

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.thyme.smalam119.kountries.KountryDetail.KountryDetailActivity
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.Network.ApiService
import com.thyme.smalam119.kountries.R
import com.thyme.smalam119.kountries.Utils.Cons
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RandomFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    var progressBar: ProgressBar? = null
    var countryNameTV: TextView? = null
    var countryNameOfficialTV: TextView? = null
    var capitalNameTV: TextView? = null
    var flagImageView: ImageView? = null
    var swipeLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var v = inflater!!.inflate(R.layout.fragment_random_kountry, container, false)
        prepareView(v)
        makeGetRandomCountryNetworkCall()
        return  v
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    fun prepareView(view: View) {
        countryNameTV = view.findViewById<TextView>(R.id.country_name_text_view)
        countryNameOfficialTV = view.findViewById<TextView>(R.id.country_name_official_text_view)
        capitalNameTV = view.findViewById<TextView>(R.id.capital_text_view)
        progressBar = view.findViewById(R.id.progressBar)

        flagImageView = view.findViewById<ImageView>(R.id.flag_image_view)
        flagImageView!!.scaleType = ImageView.ScaleType.FIT_XY
        prepareSwipeRefresh(view)
    }

    fun prepareSwipeRefresh(view: View){
        swipeLayout = view.findViewById(R.id.swipe_layout)
        swipeLayout!!.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

        var onRefreshListener = object :SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                makeGetRandomCountryNetworkCall()
            }

        }
        swipeLayout!!.setOnRefreshListener(onRefreshListener)
    }

    fun bindData(kountry: Kountry) {
        countryNameTV!!.text = kountry.name
        if (kountry.altSpellings.size >= 2) {
            countryNameOfficialTV!!.text = kountry.altSpellings[1]
        } else {
            countryNameOfficialTV!!.text = "N/A"
        }
        capitalNameTV!!.text = kountry.capital

        Picasso
                .with(context)
                .load(Cons.BASE_URL_FLAG + kountry.alpha2Code + ".png")
                .into(flagImageView!!)

        flagImageView!!.setOnClickListener({ v ->
            val intent = Intent(context, KountryDetailActivity::class.java)
            intent.putExtra(Cons.ALPHA_2_CODE_EXTRA,kountry.alpha2Code)
            ContextCompat.startActivity(context, intent, null)
        })
    }

    fun makeGetRandomCountryNetworkCall() {
        var randomKountry: Kountry? = null
        progressBar!!.visibility = View.VISIBLE
        if (swipeLayout!!.isRefreshing) {
            swipeLayout!!.setRefreshing(false)
        }
        var apiService = ApiService.create()
        apiService.getAllKountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    progressBar!!.visibility = View.GONE
                    randomKountry = result.get(Cons.randInt(1,256))
                    bindData(randomKountry!!)
                }, { error ->
                    progressBar!!.visibility = View.GONE
                    error.printStackTrace()
                    randomKountry = null
                })
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): RandomFragment {
            val fragment = RandomFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
