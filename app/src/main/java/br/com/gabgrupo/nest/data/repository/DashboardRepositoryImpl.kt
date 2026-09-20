package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.model.DashboardGroupResponse
import br.com.gabgrupo.nest.data.remote.DashboardApiService
import br.com.gabgrupo.nest.data.remote.ApiErrorMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val apiService: DashboardApiService
) : DashboardRepository {

    override suspend fun getDashboard(): Result<DashboardResponse> {
        return try {
            val response = apiService.getDashboard()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta vazia do servidor."))
                }
            } else {
                    Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "o dashboard")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }

    override suspend fun getByGuideline(): Result<List<DashboardGroupResponse>> = fetchGroups(
        request = { apiService.getByGuideline() },
        message = "Erro ao carregar métricas por guideline."
    )

    override suspend fun getByProject(): Result<List<DashboardGroupResponse>> = fetchGroups(
        request = { apiService.getByProject() },
        message = "Erro ao carregar métricas por projeto."
    )

    private suspend fun fetchGroups(
        request: suspend () -> retrofit2.Response<List<DashboardGroupResponse>>,
        message: String
    ): Result<List<DashboardGroupResponse>> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Resposta vazia do servidor."))
            } else {
                Result.failure(Exception(ApiErrorMessage.forStatus(response.code(), "as métricas")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}