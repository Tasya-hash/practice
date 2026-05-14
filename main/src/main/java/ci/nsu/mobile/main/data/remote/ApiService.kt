package ci.nsu.mobile.main.data.remote

import ci.nsu.mobile.main.data.models.*
import retrofit2.http.*
import retrofit2.Response

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("/api/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>
}
