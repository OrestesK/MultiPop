package orestesk.multipop

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid")
    val U_Id: Int?,
    @SerializedName("username")
    val UserName: String,
    @SerializedName("userpassword")
    val UserPassword: String)

data class Sound(
    @SerializedName("uid")
    val U_Id: Int?,
    @SerializedName("posterusername")
    val PosterUserName: String,
    @SerializedName("soundname")
    val SoundName: String,
    @SerializedName("description")
    val Description: String?,
    @SerializedName("datetime")
    val Datetime: String?,
    @SerializedName("length")
    val Length: String?,
    @SerializedName("file")
    val File: String)
