package com.thyme.smalam119.kountries.KountryList

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.Network.ApiService
import com.thyme.smalam119.kountries.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KountryListFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    var recyclerVIew: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: KountryListAdapter? = null

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

        //network call for getting country list
        makeGetAllCountryNetworkCall()
        return view
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

    fun prepareViews(view: View) {
        prepareRecyclerView(view)
        prepareProgressBar(view)
    }

    fun prepareRecyclerView(view: View) {
        recyclerVIew = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerVIew!!.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
    }

    fun prepareProgressBar(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
    }

    fun prepareAdapter(kountryList: ArrayList<Kountry>) {
        adapter = KountryListAdapter(kountryList)
        recyclerVIew!!.adapter = adapter
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
