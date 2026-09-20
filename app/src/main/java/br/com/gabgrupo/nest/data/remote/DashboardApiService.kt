package br.com.gabgrupo.nest.data.remote

import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.model.DashboardGroupResponse
import retrofit2.http.GET
import retrofit2.Response

interface DashboardApiService {
    @GET("/api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>

    @GET("/api/dashboard/by-guideline")
    suspend fun getByGuideline(): Response<List<DashboardGroupResponse>>

    @GET("/api/dashboard/by-project")
    suspend fun getByProject(): Response<List<DashboardGroupResponse>>
}