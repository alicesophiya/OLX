package com.sofi.olxapplication.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    var sharedPref: SharedPreferences
    init{
        sharedPref = context.getSharedPreferences(Constants.SharedPrefName, Context.MODE_PRIVATE)

    }

    fun setString(key: String, value: String) {
        sharedPref.edit().putString(key, value).commit()
    }
    fun getString(key:String): String? {
        return sharedPref.getString(key, "")
            //okay?kk sir
    }
}