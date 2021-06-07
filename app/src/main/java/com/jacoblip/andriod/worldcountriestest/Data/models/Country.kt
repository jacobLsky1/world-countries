package com.jacoblip.andriod.worldcountriestest.Data.models

import androidx.room.Entity
import io.realm.RealmModel
import io.realm.RealmObject

@Entity
   data class Country(
        val name: String,
        val nativeName: String,
        val flag: String,
        val area: Double,
        val population: Int,
        val borders: List<String>,
        val alpha3Code: String
        /*
    val alpha2Code: String,
    val altSpellings: List<String>,
    val callingCodes: List<String>,
    val capital: String,
    val cioc: String,
    val currencies: List<Currency>,
    val demonym: String,
    val gini: Double,
    val languages: List<Language>,
    val latlng: List<Double>,
    val numericCode: String,
    val region: String,
    val regionalBlocs: List<RegionalBloc>,
    val subregion: String,
    val timezones: List<String>,
    val topLevelDomain: List<String>,
    val translations: Translations

         */


)