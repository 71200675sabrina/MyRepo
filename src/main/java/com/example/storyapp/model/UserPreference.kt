package com.example.storyapp.model

import android.content.Context
import android.util.Log

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getDataLogin(): UserModel{
        return UserModel(
            preferences.getString(NAME_KEY, "").toString(),
            preferences.getString(EMAIL_KEY, "").toString(),
            preferences.getString(TOKEN_KEY, "").toString(),
            preferences.getBoolean(STATE_KEY, false)
        )
    }

    fun setLogin(userModel : UserModel){
        val editor = preferences.edit()
        editor.putString(NAME_KEY, userModel.name)
        editor.putString(EMAIL_KEY, userModel.email)
        editor.putString(TOKEN_KEY, userModel.token)
        editor.putBoolean(STATE_KEY, userModel.isLogin)
        editor.apply()
        }

    fun login() {
        val editor = preferences.edit()
        editor.putBoolean(STATE_KEY, true)
        editor.apply()
    }

    fun logout(){
        val editor = preferences.edit()
        editor.remove(NAME_KEY)
        editor.remove(EMAIL_KEY)
        editor.remove(TOKEN_KEY)
        editor.putBoolean(STATE_KEY, false)
        editor.apply()

        Log.d("STATE_KEY", "Nilai STATE_KEY setelah logout: ${preferences.getBoolean(STATE_KEY, false)}")
    }


    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null

        const val PREFS_NAME = "login_pref"
        const val NAME_KEY = "NAME"
        const val TOKEN_KEY = "TOKEN"
        const val EMAIL_KEY = "EMAIL"
        const val STATE_KEY = "STATE"

        fun getInstance(context: Context) : UserPreference {
            if (INSTANCE == null) {
                INSTANCE = UserPreference(context)
            }
            return INSTANCE as UserPreference
        }
    }

}
