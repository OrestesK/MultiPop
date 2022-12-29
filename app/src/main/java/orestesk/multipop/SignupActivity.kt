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
        lifecycleScope.launch(Dispatchers.Main) {
            val usr : User?
            try {
                usr = getUserByUserName(email)
            } catch (e: ConnectException)
            {
                LoginActivity.makeToast(this@SignupActivity, "DATABASE OFFLINE")
                return@launch
            }


            if(usr != null) {
                passwordField.setText("")
                LoginActivity.makeToast(this@SignupActivity, "User Already Exists")
            }
            else {
                if (createUser(null, email, passwordField.text.toString()) == null){
                    LoginActivity.makeToast(this@SignupActivity, "Signup Unsuccessful")
                }
                else{
                    LoginActivity.makeToast(this@SignupActivity, "Signup Successful")
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                }
            }
        }
    }
    private fun validateInput() : Boolean {
        if (!LoginActivity.validateInput(emailField, passwordField)) {
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