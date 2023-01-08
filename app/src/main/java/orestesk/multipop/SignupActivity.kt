package orestesk.multipop

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class SignupActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var repasswordField: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        repasswordField = findViewById(R.id.repasswordField)

        btn_signup.setOnClickListener {
                if(validateInput()){
                    signup()}
            }
        }

    private fun signup(){
        val email = emailField.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val usr : User?
            try {
                usr = getUserByUserName(email)
            } catch (e: ConnectException) {
                launch(Dispatchers.Main) {
                    makeToast(this@SignupActivity, "DATABASE OFFLINE")
                }
                return@launch
            }

            launch(Dispatchers.Main) {
                usr?.let {passwordField.setText("")
                    makeToast(this@SignupActivity, "User Already Exists")
                } ?: run{
                    createUser(null, email, passwordField.text.toString())?.let {
                        makeToast(this@SignupActivity, "Signup Successful")
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    } ?: run {
                        makeToast(this@SignupActivity, "Signup Unsuccessful")
                    }
                }
            }
        }
    }
    private fun validateInput() : Boolean {
        if (!validateUserCredentials(emailField, passwordField)) {
            return false
        }
        if (passwordField.text.toString() != repasswordField.text.toString()) {
            repasswordField.error = "Passwords Do Not Match"
            return false
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}