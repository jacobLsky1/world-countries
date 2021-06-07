package com.androiddevs.mvvmnewsapp.api

import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CountriesAPI {

    @GET("/rest/alpha{code}")
    suspend fun getCountry(
        @Path("code")code:String
    ):Response<ArrayList<Country>>

    @GET("/rest/v2/all?fields=name;nativeName;flag;area;population;borders;alpha3Code")
    suspend fun getAllCountries():Response<ArrayList<Country>>


}