package br.com.gabgrupo.nest.data.remote

import br.com.gabgrupo.nest.data.model.DashboardResponse
import retrofit2.http.GET
import retrofit2.Response

interface DashboardApiService {
    @GET("/api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>
}