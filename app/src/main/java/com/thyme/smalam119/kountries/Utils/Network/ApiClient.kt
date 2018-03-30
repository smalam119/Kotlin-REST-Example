package com.thyme.smalam119.kountries.Utils.Network

import com.thyme.smalam119.kountries.Utils.AppConstants
import com.thyme.smalam119.kountries.Model.Kountry
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by smalam119 on 1/30/18.
 */
interface ApiClient {
    @GET("rest/v2/all")
    fun getAllKountries(): Observable<ArrayList<Kountry>>

    @GET("rest/v2/alpha/{code}")
    fun getKountry(@Path("code") alpha2Code: String): Observable<Kountry>

    companion object Factory {
        fun create(): ApiClient {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppConstants.BASE_URL)
                    .build()

            return retrofit.create(ApiClient::class.java)
        }
    }
}