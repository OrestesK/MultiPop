package orestesk.multipop

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.system.measureTimeMillis

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val retrofit = Instances.RetrofitInstance.getInstance()
    private val apiInterface: SoundService = retrofit.create(SoundService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet.setOnClickListener {
            doApiRequests()
        }

        btnPost.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
                textView.text = "HUH"
            }
        }
    }

    /*
    private suspend fun doExpensiveWork() = withContext(Dispatchers.Default) {
        Log.i(TAG, "doExpensiveWork coroutine thread: ${Thread.currentThread().name}")
        val timeTakenMillis = measureTimeMillis { BigInteger.probablePrime(2200, Random()) }
        "Time taken (ms): $timeTakenMillis"
    }

     */


    private fun doApiRequests() {
        lifecycleScope.launchWhenCreated {
            try {
                //val usr: User = User(uid = null, username = "", userpassword = "kotlinpass")
                //apiInterface.postUser(usr)

                val userResponse = apiInterface.getUserByUsername("TestUser")
                //val postsByUser = blogService.getSoundsByPosterUserName("TestUser")

                println(userResponse.body())
                if (userResponse.isSuccessful){
                    "User ${userResponse.body()?.UserName} ${userResponse.body()?.UserPassword}".also { textView.text = it }
                }

            } catch (exception: Exception) {
                Log.e(TAG, "Exception $exception")
            }
        }
    }

    private suspend fun createUser(uid: Int, username: String, tempPassword: String){
        val creationResponse = apiInterface.postUser(User(uid, username, hashSha256(tempPassword)))

        if(creationResponse.isSuccessful){

        }
        else{
            println("INVALID USER OR USERNAME ALREADY TAKEN")
        }

    }

    private suspend fun createSound(uid: Int, postUserName: String, soundName: String, description: String, datetime: String, length: String, file : ByteArray){
        val creationResponse = apiInterface.postSound(Sound(uid ,postUserName, soundName, description, datetime, length, file))

        if(creationResponse.isSuccessful){

        }
        else{
            println("INVALID SOUND OR SOUNDNAME ALREADY TAKEN")
        }

    }

    private fun hashSha256(string: String): String {
        var digest: MessageDigest? = null
        var hash = ""
        try {
            digest = MessageDigest.getInstance("SHA-256")
            digest.update(string.toByteArray())
            val bytes: ByteArray = digest.digest()
            val sb = StringBuffer()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(0xFF and bytes[i].toInt())
                if (hex.length == 1) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            hash = sb.toString()
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        }
        return hash
    }

}
