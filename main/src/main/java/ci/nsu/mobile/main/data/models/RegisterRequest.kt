package ci.nsu.mobile.main.data.models

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)