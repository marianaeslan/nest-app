package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.ProjectRequest
import br.com.gabgrupo.nest.data.model.ProjectResponse
import br.com.gabgrupo.nest.data.model.ProjectSummary
import br.com.gabgrupo.nest.data.remote.ProjectApiService
import br.com.gabgrupo.nest.data.remote.ApiErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apiService: ProjectApiService
) : ProjectRepository {

    override suspend fun getAll(): Result<List<ProjectSummary>> {
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "os projetos")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getOverview(): Result<List<ProjectSummary>> {
        return try {
            val response = apiService.getOverview()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "a visão geral de projetos")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getById(id: String): Result<ProjectResponse> {
        return try {
            val response = apiService.getById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o projeto")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun create(request: ProjectRequest): Result<ProjectResponse> {
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o projeto")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun update(id: String, request: ProjectRequest): Result<ProjectResponse> {
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
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o projeto")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}