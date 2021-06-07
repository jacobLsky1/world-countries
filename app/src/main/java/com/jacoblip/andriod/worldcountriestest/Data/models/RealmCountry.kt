package com.jacoblip.andriod.worldcountriestest.Data.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmCountry(
        @PrimaryKey
        var number:Int = 0,
    @SerializedName("name") var name:String = "",
    @SerializedName("native") var nativeName:String = "",
    @SerializedName("picture") var picture:String = "",
    @SerializedName("size") var size:Double = 0.0,
    @SerializedName("borders") var borders: String = "",
    @SerializedName("poulation") var population:Int = 0,
    @SerializedName("codee") var code:String = ""
):RealmObject(),Comparable<RealmCountry>{

    override fun compareTo(other: RealmCountry): Int {
        if(size<other.size)
            return 1
        return if(size==other.size)
            0
        else
            -1
    }
}
