package orestesk.multipop

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

const val MIN_PASSWORD_LENGTH = 2

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginActivity.appContext = applicationContext

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)

        btn_login.setOnClickListener {
            //startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            if(LoginActivity.validateInput(emailField, passwordField)){
                login()
            }
        }
        btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun login() {
        val email = emailField.text.toString()
        lifecycleScope.launch(Dispatchers.Main) {
            val usr : User?
            try {
                usr = getUserByUserName(email)
            } catch (e: ConnectException)
            {
                makeToast(this@LoginActivity, "DATABASE OFFLINE")
                return@launch
            }

            if(usr == null) {
                passwordField.setText("")
                makeToast(this@LoginActivity, "User Does Not Exist")
            } else{
                val password = passwordField.text.toString()
                if (hashSha256(password) == usr.UserPassword) {
                    Toast.makeText(this@LoginActivity, "Wrong Password", Toast.LENGTH_SHORT).show()
                } else{
                    LoginActivity.usr = usr
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        }
    }


    companion object{
        lateinit var usr : User
        lateinit  var appContext: Context

        fun validateInput(email: EditText, password:EditText): Boolean {
            if (email.text.toString().trim() == "") {
                email.error = "Please Enter Email"
                return false
            }
            if (password.text.toString() == "") {
                password.error = "Please Enter Password"
                return false
            }
            /*
            if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
                emailField.error = "Please Enter Valid Email (doesn't have to exist ;)"
                return false
            }
             */
            if (password.text.length < MIN_PASSWORD_LENGTH) {
                password.error = "Password must be at least $MIN_PASSWORD_LENGTH characters"
                return false
            }
            return true
        }

        fun makeToast(activity : Context, message : String){
            val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        }
    }
}