package com.jacoblip.andriod.worldcountriestest.Views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.R
import com.jacoblip.andriod.worldcountriestest.Views.CountriesRVFragment

class CountryAdapter(var countries:Array<RealmCountry>,var callbacks: CountriesRVFragment.Callbacks?):RecyclerView.Adapter<CountryItemViewHolder>() {
    var i = callbacks
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        when(viewType){
        }
        Log.i("Adapter","CreateViewHolder")
        return CountryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false))
    }

    override fun getItemCount():Int {
        Log.i("Adapter","itemCount, ${countries.size}")
       return countries.size
    }

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        Log.i("Adapter","$position")
        var country = countries[position]
        holder.itemView.apply {
            val countryNameTV: TextView = findViewById(R.id.countryFragmentNameTV)
            val nativeCountryName: TextView = findViewById(R.id.nativeCountryFragmentName)
            var countryFlag: ShapeableImageView = findViewById(R.id.countryFragmentImage)
            countryNameTV.text = country.name
            nativeCountryName.text = country.nativeName
            //Glide.with(this).load(country.picture).into(countryFlag)
            countryFlag.loadUrl(country.picture)

            if(callbacks!=null) {
                countryNameTV.setOnClickListener{
                    callbacks!!.onCountrySelected(country)
                }
                nativeCountryName.setOnClickListener{
                    callbacks!!.onCountrySelected(country)
                }
                countryFlag.setOnClickListener{
                    callbacks!!.onCountrySelected(country)
                }
                setOnClickListener {
                    callbacks!!.onCountrySelected(country)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(position){

        }
        return 0
    }



    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .placeholder(R.drawable.ic_world)
            .error(R.drawable.ic_world)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }



}