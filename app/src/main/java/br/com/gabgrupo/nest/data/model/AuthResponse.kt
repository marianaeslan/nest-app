package br.com.gabgrupo.nest.data.model

data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String,
    val role: UserRole
)
