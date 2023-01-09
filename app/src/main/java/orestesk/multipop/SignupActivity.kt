package orestesk.multipop

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
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

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        repasswordField = findViewById(R.id.repasswordField)

        btn_signup.setOnClickListener {
                if(validateInput()){
                    signup(emailField.text.toString(), passwordField.text.toString())
                }
            }
        }

    private fun signup(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usr: User?
            try {
                usr = getUserByUserName(email)
            } catch (e: ConnectException) {
                launch(Dispatchers.Main) {
                    makeToast(this@SignupActivity, "DATABASE OFFLINE")
                }
                return@launch
            }
            loginMessage(usr, email, password, this)
        }
    }

    private fun loginMessage(usr:User?, email: String, password: String, scope: CoroutineScope) {
        scope.launch(Dispatchers.Main) {
            usr?.let {
                passwordField.setText("")
                makeToast(this@SignupActivity, "User Already Exists")
            } ?: run {
                createUser(null, email, hashSha256(password))?.let {
                    makeToast(this@SignupActivity, "Signup Successful")
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                } ?: run {
                    makeToast(this@SignupActivity, "Signup Unsuccessful")
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