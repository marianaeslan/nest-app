package br.com.gabgrupo.nest.data.model

data class AuthResponse(
    val token: String,
    val userId: Long,
    val name: String,
    val role: UserRole
)

