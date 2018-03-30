package com.thyme.smalam119.kountries.Utils

import android.content.Context
import android.net.ConnectivityManager
import java.util.*

/**
 * Created by smalam119 on 1/30/18.
 */
class AppConstants {
    companion object {
        // base url to get country information
        val BASE_URL = "https://restcountries.eu/"
        //base url for country flag
        val BASE_URL_FLAG = "https://raw.githubusercontent.com/emcrisostomo/flags/master/png/256/"
        val ALPHA_2_CODE_EXTRA = "KOUNTRY_NAME"

        fun isConnectedWithNetwork(context: Context): Boolean {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun randInt(min: Int, max: Int): Int {

            val rand = Random()
            val randomNum = rand.nextInt(max - min + 1) + min
            return randomNum - 1
        }
    }
}