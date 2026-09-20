package br.com.gabgrupo.nest.data.model

data class CreateUserRequest(
    val name: String,
    val email: String,
    val role: UserRole
)