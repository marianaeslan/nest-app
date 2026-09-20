package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.model.AuthResponse
import br.com.gabgrupo.nest.data.model.CreateUserRequest

interface AuthRepository {
    suspend fun login(request: AuthRequest): Result<AuthResponse>
    suspend fun createUser(request: CreateUserRequest): Result<AuthResponse>
}