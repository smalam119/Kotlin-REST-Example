package com.thyme.smalam119.kountries.Modules.KountryList

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.thyme.smalam119.kountries.Utils.AppConstants
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.Utils.Network.ApiClient
import com.thyme.smalam119.kountries.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import com.thyme.smalam119.kountries.Utils.GridSpacingItemDecoration
import android.util.TypedValue
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager


class KountryListFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: KountryListAdapter? = null
    var kountryList = ArrayList<Kountry>()
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
        var view = inflater!!.inflate(R.layout.fragment_kountry_list, container, false)
        prepareViews(view)
        if(AppConstants.isConnectedWithNetwork(view.context)) {
            //network call for getting country list
            makeGetAllCountryNetworkCall()
        } else {
            showNetworkWarning(view)
        }

        return view
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    fun showNetworkWarning(view: View) {
        view.context.alert("Please connect with internet","Warning") {
            positiveButton("OK") {
                activity.finish()
            }
        }.show()
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

    fun prepareViews(view: View) {
        prepareRecyclerView(view)
        prepareProgressBar(view)
        prepareSwipeRefresh(view)
        prepareAdapter(kountryList)
    }

    fun prepareRecyclerView(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView!!.layoutManager = GridLayoutManager(view.context, 2) as RecyclerView.LayoutManager?;
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView!!.setItemAnimator(DefaultItemAnimator())
    }

    fun prepareProgressBar(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
    }

    fun prepareAdapter(kountryList: ArrayList<Kountry>) {
        adapter = KountryListAdapter(kountryList)
        recyclerView!!.adapter = adapter
    }

    fun prepareSwipeRefresh(view: View){
        swipeLayout = view.findViewById(R.id.swipe_layout)
        swipeLayout!!.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

        var onRefreshListener = object :SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                makeGetAllCountryNetworkCall()
            }

        }
        swipeLayout!!.setOnRefreshListener(onRefreshListener)
    }

    fun norifyAdapter(kountryListForNetwork: ArrayList<Kountry>) {
        kountryList.clear()
        kountryList.addAll(kountryListForNetwork)
        adapter!!.notifyDataSetChanged()
    }

    fun makeGetAllCountryNetworkCall() {
        progressBar!!.visibility = View.VISIBLE
        if (swipeLayout!!.isRefreshing) {
            swipeLayout!!.setRefreshing(false)
        }
        var apiService = ApiClient.create()
        apiService.getAllKountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    progressBar!!.visibility = View.GONE
                    norifyAdapter(result)
                }, { error ->
                    progressBar!!.visibility = View.GONE
                    error.printStackTrace()
                })
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String): KountryListFragment {
            val fragment = KountryListFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
