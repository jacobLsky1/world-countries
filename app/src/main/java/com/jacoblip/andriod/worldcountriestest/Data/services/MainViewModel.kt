package com.jacoblip.andriod.worldcountriestest.Data.services

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.utilities.Resource
import com.jacoblip.andriod.worldcountriestest.utilities.Util
import io.realm.RealmList
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response


private const val TAG = "MainViewModel"
class MainViewModel(repository: Repository,context: Context):ViewModel() {
    var repository = repository
    var context = context
    var isInitialized = false
    var hashMap:HashMap<String,RealmCountry> = hashMapOf()
    var currentFragment: Int? = null
    var currentCountry:RealmCountry? = null
    var allCountries:MutableLiveData<Resource<ArrayList<RealmCountry>>> = MutableLiveData()
    var appCountries:ArrayList<RealmCountry>? = null
    var sortedCountries :ArrayList<RealmCountry> = arrayListOf()

    fun getAllCountries() =  viewModelScope.launch {
        Log.i(TAG,"getAllCountries")
            Log.i(TAG, "fun started")
            allCountries.postValue(Resource.Loading())
            Log.i(TAG, "resource loding")
            val response = repository.getAllCountries()
            Log.i(TAG, "request made")
            allCountries.postValue(handleResponseForAllCountries(response))
            Log.i(TAG, "value posted")
            isInitialized = true
    }

    fun postAtoZ(){
        if(appCountries!=null)
        allCountries.postValue(handleData((appCountries!!)))
    }

    fun postSorted(){
        if(sortedCountries!=null)
        allCountries.postValue(handleData((sortedCountries!!)))
    }

    fun  createHashMap(countries: ArrayList<RealmCountry>){
        Log.i(TAG,"createHashMap")
        if(hashMap!!.size==0) {
            for (con in countries) {
                hashMap!![con.code] = con
            }
            Log.i(TAG, "")
        }

    }

    fun createSortedCountries(){
        Log.i(TAG,"createSortedCountries")
        if(sortedCountries.size==0) {
            var sorted = appCountries?.toMutableList()
            if (sorted != null) {
                sorted.sort()
                sortedCountries = sorted.toTypedArray().toCollection(ArrayList())
            }
        }
    }

    private fun handleResponseForAllCountries(response: Response<ArrayList<Country>>): Resource<ArrayList<RealmCountry>>? {
        Log.i(TAG,"handleResponseForAllCountries")
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                if(appCountries!!.size==0) {
                    appCountries = toRealmCountries(resultResponse)
                    createHashMap(appCountries!!)
                    createSortedCountries()
                }
                return Resource.Success(appCountries!!)
            }
        }else{
            Util.netWorkRequestFailed.postValue(true)
        }
        return Resource.Error(response.message())
    }

    fun toRealmCountries(countries: ArrayList<Country>):ArrayList<RealmCountry>{
        var realmCountries = arrayListOf<RealmCountry>()
        for(country in countries){
            var realmCountry = RealmCountry(countries.indexOf(country),country.name,country.nativeName,country.flag,country.area,makeString(country.borders),country.population,country.alpha3Code)
            realmCountries.add(realmCountry)
        }
        return realmCountries
    }

    fun makeString(borders:List<String>):String{
        var str = ""
        for(border in borders){
            str+="$border "
        }
        str.trim()
        return str
    }

    fun getBorderingCountries(mycodes:String):Array<RealmCountry>{
        var codes = mycodes.split(' ')
        var borders = arrayListOf<RealmCountry>()
        for(code in codes) {
            var border = hashMap?.get(code)
            if (border != null) {
                borders.add(border)
            }
        }
        return borders.toTypedArray()
    }



    fun getCountriesFromLocal():Array<RealmCountry>{
        if(!isInitialized) {
            Log.i(TAG, "getCountriesFromLocal")
            var local = repository.getCountriesFromLocal().toCollection(ArrayList())
            appCountries = local
            createHashMap(local)
            createSortedCountries()
            return local.toTypedArray()
        }
        return appCountries!!.toTypedArray()
    }



    fun handleData(countries:ArrayList<RealmCountry>): Resource<ArrayList<RealmCountry>>{
        return Resource.Success(countries)
    }
}