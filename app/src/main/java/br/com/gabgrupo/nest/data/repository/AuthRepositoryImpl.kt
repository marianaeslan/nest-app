package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.model.AuthResponse
import br.com.gabgrupo.nest.data.remote.AuthApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRepository {

    override suspend fun login(request: AuthRequest): Result<AuthResponse> {
        return try {

            val response = apiService.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {

                Result.failure(Exception("Falha na autenticação. Código: ${response.code()}"))
            }
        } catch (e: Exception) {

            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}