package orestesk.multipop

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException


class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        preferences = Preferences(appContext)

        if(preferences.logged){
            login(preferences.username, preferences.password)
        }

        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)

        btn_login.setOnClickListener {
            if(validateUserCredentials(emailField, passwordField)){
                login(emailField.text.toString(), passwordField.text.toString())
            }
        }

        btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun login(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usr: User?
            try {
                usr = getUserByUserName(email)
            } catch (e: ConnectException) {
                launch(Dispatchers.Main) {
                    makeToast(appContext, "DATABASE OFFLINE")
                }
                return@launch
            }

            launch(Dispatchers.Main) {
                usr?.let{
                    val hashPass = if (preferences.logged) preferences.password else hashSha256(password)
                    if (hashPass != usr.UserPassword) {
                        Toast.makeText(appContext, "Wrong Password", Toast.LENGTH_SHORT).show()
                    } else {
                        preferences.setUser(usr)
                        makeToast(appContext, "Login Successful")
                        startActivity(Intent(appContext, MainActivity::class.java))
                        finish()
                    }
                    // !! don't return null here or ?: runs
                } ?: run{
                    passwordField.setText("")
                    makeToast(appContext, "User Does Not Exist")
                }
            }
        }
    }

    companion object{
        lateinit var preferences : Preferences
        lateinit var appContext: Context
    }
}