package com.test.soultools.tool.storage

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
                return preferences.getInt(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: Int) {
                preferences.edit().putInt(property.name, value).apply()
            }
        }

        fun long(defaultValue: Long = 0) = object : ReadWriteProperty<SharedPreferencesHelper, Long> {
            override fun getValue(thisRef: SharedPreferencesHelper, property: KProperty<*>): Long {
                return preferences.getLong(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: Long) {
                preferences.edit().putLong(property.name, value).apply()
            }
        }


        fun float(defaultValue: Float = 0f) = object : ReadWriteProperty<SharedPreferencesHelper, Float> {
            override fun getValue(thisRef: SharedPreferencesHelper, property: KProperty<*>): Float {
                return preferences.getFloat(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: Float) {
                preferences.edit().putFloat(property.name, value).apply()
            }
        }

        fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<SharedPreferencesHelper, Boolean> {
            override fun getValue(thisRef: SharedPreferencesHelper, property: KProperty<*>): Boolean {
                return preferences.getBoolean(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: Boolean) {
                preferences.edit().putBoolean(property.name, value).apply()
            }
        }

        fun string(defaultValue: String = "") = object : ReadWriteProperty<SharedPreferencesHelper, String> {
            override fun getValue(thisRef: SharedPreferencesHelper, property: KProperty<*>): String {
                return preferences.getString(property.name, defaultValue)!!
            }

            override fun setValue(thisRef: SharedPreferencesHelper, property: KProperty<*>, value: String) {
                preferences.edit().putString(property.name, value).apply()
            }
        }
    }
}