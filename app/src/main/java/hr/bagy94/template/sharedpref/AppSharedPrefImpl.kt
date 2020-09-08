package hr.bagy94.template.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import hr.bagy94.android.base.const.EMPTY_STRING
import hr.bagy94.android.common.sharedpref.AppSharedPreference
import hr.bagy94.template.BuildConfig

internal class AppSharedPreferencesImpl(applicationContext: Context, private val gson: Gson): AppSharedPreference {
    private val defaultSharedPref : SharedPreferences

    init {
        val packageName = applicationContext.packageName
        defaultSharedPref = applicationContext.getSharedPreferences(
            "$packageName$DEFAULT_SHARED_PREF",
            Context.MODE_PRIVATE
        )
        if (!defaultSharedPref.contains(KEY_APP_VERSION)) {
            editor { putString(KEY_APP_VERSION, BuildConfig.VERSION_NAME) }
        }
        editor { putString(KEY_API_URL, BuildConfig.API_URL) }
    }

    override var apiURL: String
        get() = defaultSharedPref.getString(KEY_API_URL, EMPTY_STRING)!!
        set(value) {editor { putString(KEY_API_URL,value) }}

    override var appLocale: String
        get() = defaultSharedPref.getString(KEY_LANGUAGE, EMPTY_STRING)!!
        set(value) { editor { putString(KEY_LANGUAGE,value) } }


    override var appVersion: String
        get() = defaultSharedPref.getString(KEY_APP_VERSION, EMPTY_STRING)!!
        set(value) { editor { putString(KEY_APP_VERSION,value) } }

    override var authToken: String?
        get() = defaultSharedPref.getString(KEY_TOKEN, null)
        set(value) { editor { putString(KEY_TOKEN,value) } }

    protected fun editor(editor: SharedPreferences.Editor.()->Unit){
        defaultSharedPref.edit().also(editor).apply()
    }
}