package orestesk.multipop

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appContext = applicationContext

        btnGet.setOnClickListener {
            textView.text = LoginActivity.userName
        }

        btnPost.setOnClickListener {
            CreateSoundFragment().show(supportFragmentManager, "createUser")
        }

        btnSelect.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            displaySounds()
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun displaySounds(){
        lifecycleScope.launch(Dispatchers.IO) {
            val sounds = getSounds()

            launch(Dispatchers.Main) {
                sounds?.let {
                    findViewById<LinearLayout>(R.id.layout_sounds).removeAllViews()
                    for (sound in sounds) {
                        val btn = Button(appContext)
                        btn.text = sound.SoundName
                        btn.setOnClickListener {
                            playDisplayedSound(btn, sound)
                        }
                        findViewById<LinearLayout>(R.id.layout_sounds).addView(btn)
                    }
                } ?: run {
                    makeToast(appContext, "No Sounds")
                }
            }
        }
    }

    companion object{
        lateinit  var appContext: Context
    }
}