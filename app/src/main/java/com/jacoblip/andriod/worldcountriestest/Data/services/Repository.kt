package com.jacoblip.andriod.worldcountriestest.Data.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.utilities.Util
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Response
import java.util.concurrent.Executors

class Repository(context: Context) {



    suspend fun getAllCountries(): Response<ArrayList<Country>> {
        return RetrofitInstance.api.getAllCountries()
    }
    fun getCountriesFromLocal():Array<RealmCountry>{
        val realm = Realm.getDefaultInstance()
        val countries = realm.where(RealmCountry::class.java).findAll().toTypedArray()
        return countries
    }

}