 package com.jacoblip.andriod.wo

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jacoblip.andriod.worldcountriestest.Data.models.RealmCountry
import com.jacoblip.andriod.worldcountriestest.Data.services.MainViewModel
import com.jacoblip.andriod.worldcountriestest.Data.services.Repository
import com.jacoblip.andriod.worldcountriestest.Data.services.ViewModelProviderFactory
import com.jacoblip.andriod.worldcountriestest.R
import com.jacoblip.andriod.worldcountriestest.Views.CountriesRVFragment
import com.jacoblip.andriod.worldcountriestest.Views.CountryFragment
import com.jacoblip.andriod.worldcountriestest.utilities.Util
import com.jacoblip.andriod.worldcountriestest.utilities.WifiReceiver
import io.realm.Realm
import io.realm.RealmConfiguration


class MainActivity : AppCompatActivity(),CountriesRVFragment.Callbacks {

    lateinit var viewModel:MainViewModel
    lateinit var wifiReceiver:WifiReceiver
    lateinit var fragment:Fragment
    lateinit var rvfragment: CountriesRVFragment
    lateinit var countryFragment: CountryFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_WorldCountriesTest)
        setContentView(R.layout.activity_main)

        initRealm()
        setUpServices()
        setUpObservers()
        rvfragment = CountriesRVFragment.newInstance()
        countryFragment = CountryFragment()
        when(savedInstanceState?.getInt("currentFragment", 1) ?: 1){
            1->{fragment = rvfragment}
            2->{
                fragment = if(viewModel.currentCountry!=null){
                    countryFragment
                }else
                   rvfragment
            }
        }
        setFragement(fragment)
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("currentFragment",viewModel.currentFragment!!)}

    fun initRealm(){
        Realm.init(this)
        var realmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    fun setUpServices(){
        val repository = Repository(applicationContext)
        val viewModelProviderFactory = ViewModelProviderFactory(repository,applicationContext)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        wifiReceiver = WifiReceiver()
    }

    fun setUpObservers(){
        Util.netWorkRequestFailed.observe(this, Observer {
            if(it){
                val dialogView = layoutInflater.inflate(R.layout.problem_dialog, null)
                val yesButton = dialogView.findViewById(R.id.tryAgainButton) as Button

                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setView(dialogView).setCancelable(false)


                val dialog = alertDialog.create()
                dialog.show()

                yesButton.setOnClickListener {
                    viewModel.getAllCountries()
                    dialog.dismiss()
                }

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sortByName ->{
               viewModel.postAtoZ()
            }
            R.id.sortBySize->{
               viewModel.postSorted()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setFragement(fragment:Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

    }


    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiReceiver, filter)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiReceiver)
    }

    override fun onCountrySelected(country: RealmCountry) {
        viewModel.currentCountry=  country
        var fragment = CountryFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("country")
            .commit()
    }

}

// bugs - TRY AND CATCH