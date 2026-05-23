package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.remote.GuidelineApiService
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
                Result.failure(Exception("Erro ao buscar diretrizes. Código: ${response.code()}"))
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
                Result.failure(Exception("Erro ao criar diretriz. Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun update(id: Long, request: GuidelineRequest): Result<GuidelineResponse> {
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
                Result.failure(Exception("Erro ao atualizar diretriz. Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun delete(id: Long): Result<Unit> {
        return try {
            val response = apiService.delete(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro ao deletar diretriz. Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}