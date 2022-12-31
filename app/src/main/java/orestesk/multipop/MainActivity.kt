package orestesk.multipop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> handleActivityResult(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                textView.text = getUserByUserName("TestUser")?.UserName
            }
        }

        btnPost.setOnClickListener {
            val createUser = CreateSoundFragment()
            createUser.show(supportFragmentManager, "createUser")
            lifecycleScope.launch(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
                textView.text = "HUH"
            }
        }

        btnSelect.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                resultLauncher.launch(Intent.createChooser(intent, "Select a file"))
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

    private fun handleActivityResult(result: ActivityResult){
        val uri = result.data?.data
        if(uri != null) {
            if (result.resultCode == Activity.RESULT_OK) MainActivity.URI = result.data?.data
            val file = contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }
    }

    companion object{
        const val TAG = "MainActivity"
        var URI : Uri? = null
    }
}