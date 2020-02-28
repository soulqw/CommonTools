package com.qw.tools.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 *
 * @author cd5160866
 * @date 2020/2/28
 */
object SharedPreferencesHelper {

    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Test
     */
    var MY_NUMBER by SharedPreferencesDelegates.int(10)

    private object SharedPreferencesDelegates {

        fun int(defaultValue: Int = 0) = object : ReadWriteProperty<SharedPreferencesHelper, Int> {

            override fun getValue(thisRef: SharedPreferencesHelper, property: KProperty<*>): Int {
                return thisRef.preferences.getInt(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: Int) {
                thisRef.preferences.edit().putInt(property.name, value).commit()
            }
        }
    }
}