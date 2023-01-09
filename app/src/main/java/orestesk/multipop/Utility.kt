package orestesk.multipop

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.security.MessageDigest
import java.util.*

const val MIN_PASSWORD_LENGTH = 2
const val MAX_USERNAME_LENGTH = 15
const val MAX_SOUNDNAME_LENGTH = 20

fun previewSound(context: Context, uri: Uri){
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setOnCompletionListener { mp ->
        mp.reset()
        mp.release()
    }
    mediaPlayer.setDataSource(context, uri)
    mediaPlayer.prepare()
    mediaPlayer.start()
}

fun playDisplayedSound(btn :Button, sound: Sound){
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource("data:audio/mp3;base64," + sound.File)

    val timerUpdater = Thread(){
        Thread.sleep(100)
        while (mediaPlayer.isPlaying) {
            val timeLeft = (mediaPlayer.duration - mediaPlayer.currentPosition) / 1000 + 1
            btn.text = timeLeft.toString()
            if(mediaPlayer.isPlaying) Thread.sleep(1000)
        }
    }

    mediaPlayer.setOnCompletionListener { mp ->
        timerUpdater.join()
        mp.reset()
        mp.release()
        btn.text = sound.SoundName
    }

    mediaPlayer.setOnPreparedListener {
        mediaPlayer.start()
        timerUpdater.start()
    }

    mediaPlayer.prepareAsync()
}

fun makeToast(activity : Context, message : String){
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun hashSha256(string: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(string.toByteArray())
    val sb = StringBuffer()
    for (i in bytes.indices) {
        val hex = Integer.toHexString(0xFF and bytes[i].toInt())
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }
    return sb.toString()
}

fun encode64(file : ByteArray): String{
    return Base64.getEncoder().encodeToString(file)
}

fun validateUserCredentials(email: EditText, password: EditText): Boolean {
    if (email.text.toString().trim() == "") {
        email.error = "Enter Email"
        return false
    }
    if (password.text.toString() == "") {
        password.error = "Enter Password"
        return false
    }
    /*
    if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
        emailField.error = "Invalid Email"
        return false
    }
     */
    if(email.text.length > MAX_USERNAME_LENGTH){
        email.error = "Email must be at most $MAX_USERNAME_LENGTH characters"
        return false
    }
    if (password.text.length < MIN_PASSWORD_LENGTH) {
        password.error = "Password must be at least $MIN_PASSWORD_LENGTH characters"
        return false
    }
    return true
}
