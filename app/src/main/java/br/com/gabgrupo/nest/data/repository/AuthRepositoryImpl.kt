package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.model.AuthResponse
import br.com.gabgrupo.nest.data.model.CreateUserRequest
import br.com.gabgrupo.nest.data.remote.AuthApiService
import br.com.gabgrupo.nest.data.remote.ApiErrorMessage
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

                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a autenticação")))
            }
        } catch (e: Exception) {

            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun createUser(request: CreateUserRequest): Result<AuthResponse> {
        return try {
            val response = apiService.createUser(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o colaborador")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}