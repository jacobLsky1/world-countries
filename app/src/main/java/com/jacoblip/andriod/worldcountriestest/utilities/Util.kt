package com.jacoblip.andriod.worldcountriestest.utilities


import androidx.lifecycle.MutableLiveData


class Util {
    companion object{
        var hasInternet : MutableLiveData<Boolean> = MutableLiveData()
        var netWorkRequestFailed:MutableLiveData<Boolean> = MutableLiveData()
    }
}