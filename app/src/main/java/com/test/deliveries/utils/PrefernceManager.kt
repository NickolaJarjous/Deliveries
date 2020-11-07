package com.test.deliveries.utils


import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.test.deliveries.app.AppConsts
import com.test.deliveries.app.Deliveries


class PreferenceManager
// endregion

// region Constructors


private constructor() {
    init {
        pref = Deliveries.instance.getSharedPreferences(AppConsts.DELIVERIES_PREF, MODE_PRIVATE)
    }

    fun putString(key: String, value: String): PreferenceManager {
        pref.edit().putString(key, value).apply()
        return this
    }

    fun getString(key: String, def: String): String {
        return pref.getString(key, def)!!
    }

    fun putBoolean(key: String, value: Boolean): PreferenceManager {
        pref.edit().putBoolean(key, value).apply()
        return this
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        return pref.getBoolean(key, def)
    }

    fun putLong(key: String, value: Long): PreferenceManager {
        pref.edit().putLong(key, value).apply()
        return this
    }

    fun getLong(key: String, def: Long): Long {
        return pref.getLong(key, def)
    }


    fun clear(key: String): PreferenceManager {
        pref.edit().remove(key).apply()
        return this
    }

    fun clearAll(): PreferenceManager {
        pref.edit().clear().apply()
        return this
    }

    companion object {

        //region  Variables

        private lateinit var pref: SharedPreferences
        /*

     Without volatile the code doesn't work correctly with multiple threads.
      The volatile prevents memory writes from being re-ordered,
       making it impossible for other threads to read uninitialized fields of your singleton through
       the singleton's pointer.
    */

        @Volatile
        private var instance: PreferenceManager? = null

        //endregion

        // region Setter & Getter

        @Synchronized
        fun getInstance(): PreferenceManager {
            val checkInstance = instance
            if (checkInstance != null) {
                return checkInstance
            }
            return synchronized(this) {
                val checkInstanceAgain = instance
                if (checkInstanceAgain != null) {
                    checkInstanceAgain
                } else {
                    val created = PreferenceManager()
                    instance = created
                    created
                }
            }
        }
    }

    //endregion

}
