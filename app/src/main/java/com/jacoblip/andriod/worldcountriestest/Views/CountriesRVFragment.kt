package com.jacoblip.andriod.worldcountriestest.Views

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jacoblip.andriod.worldcountriestest.Data.models.Country
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.Data.services.MainViewModel
import com.jacoblip.andriod.worldcountriestest.R
import com.jacoblip.andriod.worldcountriestest.Views.adapters.CountryAdapter
import com.jacoblip.andriod.worldcountriestest.utilities.InternetConectivity
import com.jacoblip.andriod.worldcountriestest.utilities.Util
import io.realm.Realm
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class CountriesRVFragment():Fragment() {

    var countries:Array<RealmCountry>? = null
    lateinit var countryRV: RecyclerView
    var dataReady: MutableLiveData<Boolean> = MutableLiveData(false)
    lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar

    interface Callbacks {
        fun onCountrySelected(country: RealmCountry)
    }

    private var callbacks: Callbacks? = null

    //the callback functions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rv_counties,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.currentFragment = 1
        view.apply {
            countryRV = findViewById(R.id.countries_RV)
            countryRV.layoutManager = LinearLayoutManager(context)
            progressBar = findViewById(R.id.progressBar1)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.appCountries!=null){
            progressBar.visibility = View.GONE
            countries = viewModel.appCountries!!.toTypedArray()
            countryRV.adapter = CountryAdapter(countries!!, callbacks)
        }
        if(!viewModel.isInitialized) {
                countries = (viewModel.getCountriesFromLocal())
                if (countries!!.isNotEmpty()) {
                    doAsync {
                        countryRV.adapter = CountryAdapter(countries!!, callbacks)
                        progressBar.visibility = View.GONE
                    }
                } else {
                    viewModel.getAllCountries()
                }
        }
            setUpObservers()
            setUpInternetObservers(view)
    }

    fun isDataReady(){
        if(countries!=null) {
            dataReady.postValue(true)
        }
    }



    fun setUpObservers(){
        dataReady.observe(viewLifecycleOwner, Observer {
            if(it){
                setUpAdapter()
            }
        })
        


        viewModel.allCountries.observe(viewLifecycleOwner, Observer {
            if(it.data!=null) {
                countries = it.data!!.toTypedArray()
                isDataReady()
            }
        })
    }

    fun saveCountries(countries:Array<RealmCountry>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.insertOrUpdate(countries.toMutableList())
        realm.commitTransaction()
    }

    fun setUpInternetObservers(view: View){
        val snackBar: Snackbar =
            Snackbar.make(view, "Can't Connect To Web..", Snackbar.LENGTH_INDEFINITE)
                .setAction("GO TO SETTINGS") {
                    context?.let { it1 -> InternetConectivity.connectToInternet(requireContext()) }
                }
        Util.hasInternet.observe(viewLifecycleOwner, Observer { it ->
            if (!it) {
                snackBar.show()
            } else {
                snackBar.dismiss()
                if(!viewModel.isInitialized&&countries==null)
                    viewModel.getAllCountries()
            }
        })

    }



    fun setUpAdapter(){
            countryRV.adapter = CountryAdapter(countries!!, callbacks!!)
            progressBar.visibility = View.GONE
            viewModel.isInitialized = true
            saveCountries(countries!!)
    }



    companion object{
        fun newInstance():CountriesRVFragment{
            return CountriesRVFragment()
        }
    }
}