package orestesk.multipop
import retrofit2.Response
import retrofit2.http.*

interface SoundService {
    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/sounds")
    suspend fun getSounds(): Response<List<Sound>>

    @GET("/users/{username}")
    suspend fun getUserByUsername(@Path("username") userName: String): Response<User>

    @GET("/sounds/{posterUserName}")
    suspend fun getSoundsByPosterUserName(@Path("posterUserName") posterUserName: String): Response<List<Sound>>

    @POST("/users")
    suspend fun postUser(@Body usr: User): Response<User>

    @POST("/sounds")
    suspend fun postSound(@Body snd: Sound): Response<Sound>
}
