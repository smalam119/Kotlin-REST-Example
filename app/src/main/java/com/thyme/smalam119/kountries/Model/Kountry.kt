package com.thyme.smalam119.kountries.Model

/**
 * Created by smalam119 on 1/30/18.
 */
data class Kountry (
    val name: String,
    val capital: String,
    val alpha2Code: String,
    val region: String,
    val subregion: String,
    val altSpellings: ArrayList<String>,
    val population: Long,
    val latlng: ArrayList<Double>,
    val area: Double,
    val timezones: ArrayList<String>,
    val currencies: ArrayList<Currency>,
    val languages: ArrayList<Language>,
    val flag: String
)

data class Currency (
       val code: String,
       val name: String,
       val symbol: String
)

data class Language (
        val name: String,
        val nativeName: String
)
