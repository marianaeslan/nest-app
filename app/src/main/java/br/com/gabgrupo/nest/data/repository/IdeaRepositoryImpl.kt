package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaReviewRequest
import br.com.gabgrupo.nest.data.remote.IdeaApiService
import br.com.gabgrupo.nest.data.remote.ApiErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryImpl @Inject constructor(
    private val apiService: IdeaApiService
) : IdeaRepository {

    override suspend fun getAll(): Result<List<IdeaResponse>> {
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "as ideias")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getMyIdeas(): Result<List<IdeaResponse>> {
        return try {
            val response = apiService.getMyIdeas()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "suas ideias")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getOverview(): Result<List<IdeaResponse>> {
        return try {
            val response = apiService.getOverview()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a visão geral de ideias")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun create(request: IdeaRequest): Result<IdeaResponse> {
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a ideia")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun update(id: String, request: IdeaRequest): Result<IdeaResponse> {
        return try {
            val response = apiService.update(id, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a ideia")))
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a ideia")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun review(id: String, request: IdeaReviewRequest): Result<IdeaResponse> {
        return try {
            val response = apiService.review(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a revisão")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}