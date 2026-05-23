package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.remote.DashboardApiService
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
                Result.failure(Exception("Erro ao carregar dashboard. Código: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: verifique sua internet."))
        }
    }
}