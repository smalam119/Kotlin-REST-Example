package com.thyme.smalam119.kountries.CountryList

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import com.squareup.picasso.Picasso
import com.thyme.smalam119.kountries.Cons
import com.thyme.smalam119.kountries.KountryDetailActivity
import com.thyme.smalam119.kountries.Model.Kountry
import com.thyme.smalam119.kountries.R

/**
 * Created by smalam119 on 1/29/18.
 */
class KountryListAdapter(val kountryList: ArrayList<Kountry>): RecyclerView.Adapter<KountryListHolder>() {
    var kountryListFiltered = kountryList

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): KountryListHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.kountry_list_cell, parent, false)
        return KountryListHolder(v)
    }

    override fun onBindViewHolder(holder: KountryListHolder?, position: Int) {
        var kountry = kountryListFiltered[position]
        holder?.countryNameTV?.text = kountry.name
        if (kountry.altSpellings.size >= 2) {
            holder?.countryNameOfficialTV?.text = kountry.altSpellings[1]
        } else {
            holder?.countryNameOfficialTV?.text = "N/A"
        }
        holder?.capitalNameTV?.text = kountry.capital
        Picasso
                .with(holder?.itemView?.context)
                .load(Cons.BASE_URL_FLAG + kountry.alpha2Code + ".png")
                .into(holder?.flagImageView)
        holder?.itemView!!.setOnClickListener({ v ->
            val intent = Intent(holder?.itemView!!.context, KountryDetailActivity::class.java)
            intent.putExtra(Cons.ALPHA_2_CODE_EXTRA,kountry.alpha2Code)
            startActivity(holder?.itemView!!.context,intent,null)
        })
    }

    override fun getItemCount(): Int {
        return kountryListFiltered.size
    }

     fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                Log.d("MainAdapter", charString)
                if (charString.isEmpty()) {
                    kountryListFiltered = kountryList
                } else {
                    var filteredList = ArrayList<Kountry>()
                    for (row in kountryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList!!.add(row)
                            Log.d("MainAdapterMatcher", row.name)
                        }
                    }

                    kountryListFiltered = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = kountryListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                kountryListFiltered = filterResults.values as ArrayList<Kountry>
                Log.d("MainAdapterPublished", kountryListFiltered.size.toString())
                notifyDataSetChanged()
            }
        }
    }
}