package orestesk.multipop

import com.google.gson.annotations.SerializedName
data class User(
    //FUCK UNSIGNED INTS OMG SPENT A DAY DEBUGGING THIS BECAUSE U_ID WAS UINT
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
    val Posterusername: String,
    @SerializedName("soundname")
    val Soundname: String,
    @SerializedName("description")
    val Description: String?,
    @SerializedName("datetime")
    val Datetime: String?,
    @SerializedName("length")
    val length: String?,
    @SerializedName("file")
    val File: ByteArray)
