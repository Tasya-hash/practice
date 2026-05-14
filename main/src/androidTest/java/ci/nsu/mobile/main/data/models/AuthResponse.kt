package ci.nsu.mobile.main.data.models

data class AuthResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val username: String
)