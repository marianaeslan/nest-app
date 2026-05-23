package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.model.AuthResponse

interface AuthRepository {
    suspend fun login(request: AuthRequest): Result<AuthResponse>
}