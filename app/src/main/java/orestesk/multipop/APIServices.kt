package orestesk.multipop

private val retrofit = Instances.RetrofitInstance.getInstance()
private val apiInterface: SoundService = retrofit.create(SoundService::class.java)

suspend fun getUsers(): List<User>?{
    val usersResponse = apiInterface.getUsers()
    return if (usersResponse.isSuccessful) usersResponse.body() else null
}
suspend fun getSounds(): List<Sound>?{
    val soundsResponse = apiInterface.getSounds()
    return if (soundsResponse.isSuccessful) soundsResponse.body() else null
}
suspend fun getUserByUserName(username: String) : User? {
    val userResponse = apiInterface.getUserByUsername(username)
    return if (userResponse.isSuccessful) userResponse.body() else null
}
suspend fun getSoundsByPosterUserName(posterUserName: String): List<Sound>?{
    val soundsResponse = apiInterface.getSoundsByPosterUserName(posterUserName)
    return if (soundsResponse.isSuccessful) soundsResponse.body() else null
}

suspend fun createUser(uid: Int?, username: String, tempPassword: String) : User?{
    val creationResponse = apiInterface.postUser(User(uid, username, hashSha256(tempPassword)))
    return if (creationResponse.isSuccessful) creationResponse.body() else null
}

suspend fun createSound(
    uid: Int?,
    postUserName: String,
    soundName: String,
    description: String?,
    datetime: String?,
    length: String?,
    file: ByteArray
) : Sound?{
    val creationResponse = apiInterface.postSound(
        Sound(
            uid,
            postUserName,
            soundName,
            description,
            datetime,
            length,
            file
        )
    )
    return if (creationResponse.isSuccessful) creationResponse.body() else null
}