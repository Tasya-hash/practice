package ci.nsu.mobile.main.data.models

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null
)
