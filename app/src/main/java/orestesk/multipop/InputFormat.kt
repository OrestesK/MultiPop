package orestesk.multipop

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun hashSha256(string: String): String {
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