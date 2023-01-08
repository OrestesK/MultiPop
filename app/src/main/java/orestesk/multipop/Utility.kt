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

// if I want to overlap sounds then create a new mediaPlayer for each sound, if you do also call release()
val mediaPlayer = MediaPlayer()

fun createInstantSound(context: Context, uri: Uri){
    mediaPlayer.reset()
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
    val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    toast.show()
}

fun hashSha256(string: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(string.toByteArray())
    val bytes = digest.digest()
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

fun decode64(file: String): ByteArray{
    return Base64.getDecoder().decode(file)
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
