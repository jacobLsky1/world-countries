package com.jacoblip.andriod.worldcountriestest.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.Data.services.MainViewModel
import com.jacoblip.andriod.worldcountriestest.R
import com.jacoblip.andriod.worldcountriestest.Views.adapters.CountryAdapter

class CountryFragment():Fragment() {

    var country :RealmCountry? = null
    var borderCountries:Array<RealmCountry>? = null
    lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    lateinit var countryImage:ShapeableImageView
    lateinit var countryName:TextView
    lateinit var countryNativeName:TextView
    lateinit var bordersTV:TextView
    lateinit var populationTV:TextView
    lateinit var areaTV:TextView
    lateinit var borderRv:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_country,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.currentFragment = 2
        country = viewModel.currentCountry
        if(country!=null) {
            var borders = viewModel.getBorderingCountries(country!!.borders)
            view.apply {
                countryName = findViewById(R.id.countryFragmentNameTV)
                countryNativeName = findViewById(R.id.nativeCountryFragmentName)
                bordersTV = findViewById(R.id.borderCountriesTV)
                countryImage = findViewById(R.id.countryFragmentImage)
                progressBar = findViewById(R.id.progressBar2)
                borderRv = findViewById(R.id.borderingCountries_RV)
                areaTV = findViewById(R.id.AreaTV)
                populationTV = findViewById(R.id.PopulationTV)
                borderRv.layoutManager = LinearLayoutManager(context)

                countryName.text = country!!.name
                countryNativeName.text = country!!.nativeName
                populationTV.text = country!!.population.toString()
                areaTV.text = "${country!!.size} sqK"
                countryImage.loadUrl(country!!.picture)
                bordersTV.text = "${country!!.name} has ${borders.size} bordering countries:"
            }
            borderRv.adapter = CountryAdapter(borders, null)
            progressBar.visibility = View.GONE
        }
        return view
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


    companion object{
        fun newInstance():CountryFragment{
            return CountryFragment()
        }
    }
}