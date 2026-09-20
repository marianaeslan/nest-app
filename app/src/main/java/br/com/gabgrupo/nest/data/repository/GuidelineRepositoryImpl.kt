package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.model.GuidelineHistory
import br.com.gabgrupo.nest.data.remote.GuidelineApiService
import br.com.gabgrupo.nest.data.remote.ApiErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuidelineRepositoryImpl @Inject constructor(
    private val apiService: GuidelineApiService
) : GuidelineRepository {

    override suspend fun getAll(): Result<List<GuidelineResponse>> {
        return try {
            val response = apiService.getAll()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "as diretrizes")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun create(request: GuidelineRequest): Result<GuidelineResponse> {
        return try {
            val response = apiService.create(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a diretriz")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun update(id: String, request: GuidelineRequest): Result<GuidelineResponse> {
        return try {
            val response = apiService.update(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a diretriz")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return try {
            val response = apiService.delete(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a diretriz")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getHistory(id: String): Result<List<GuidelineHistory>> {
        return try {
            val response = apiService.getHistory(id)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o histórico")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}