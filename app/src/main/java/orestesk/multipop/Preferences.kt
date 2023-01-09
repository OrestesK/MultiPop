package orestesk.multipop

import android.content.Context
import android.content.SharedPreferences

private const val usernameKey = "username"
private const val passwordKey = "password"
private const val loggedKey = "logged"

class Preferences(appContext: Context) {
    private val preferences: SharedPreferences = appContext.getSharedPreferences("User Preferences", Context.MODE_PRIVATE)

    fun clear(){
        preferences.edit().clear().apply()
    }

    var logged: Boolean
        get() = preferences.getBoolean(loggedKey, false)
        set(value) = preferences.edit().putBoolean(loggedKey, value).apply()

    var username: String
        get() = preferences.getString(usernameKey, "")!!
        set(value) = preferences.edit().putString(usernameKey, value).apply()

    var password: String
        get() = preferences.getString(passwordKey, "")!!
        set(value) = preferences.edit().putString(passwordKey, value).apply()

    fun setUser(usr: User){
        if(!logged) {
            logged = true
            username = usr.UserName
            password = usr.UserPassword
        }
    }
}