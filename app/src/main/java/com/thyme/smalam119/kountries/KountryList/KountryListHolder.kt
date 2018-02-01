package com.thyme.smalam119.kountries.KountryList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.thyme.smalam119.kountries.R

/**
 * Created by smalam119 on 1/29/18.
 */
class KountryListHolder(view: View): RecyclerView.ViewHolder(view) {
    var countryNameTV: TextView? = null
    var countryNameOfficialTV: TextView? = null
    var capitalNameTV: TextView? = null
    var flagImageView: ImageView? = null

    init {
        prepareView(view)
    }

    fun prepareView(view:View) {
        countryNameTV = view.findViewById<TextView>(R.id.country_name_text_view)
        countryNameOfficialTV = view.findViewById<TextView>(R.id.country_name_official_text_view)
        capitalNameTV = view.findViewById<TextView>(R.id.capital_text_view)
        flagImageView = view.findViewById<ImageView>(R.id.flag_image_view)

    }
}