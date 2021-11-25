package com.example.smartplug

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_IP_ADDRESS = "prefIPAddress"


        private var ourInstance: PreferencesManager? = null
        fun getInstance(context: Context): PreferencesManager {
            if (ourInstance == null) {
                ourInstance = PreferencesManager(context)
            }
            return ourInstance!!
        }
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        return preferences.getString(key, defValue)
    }


}