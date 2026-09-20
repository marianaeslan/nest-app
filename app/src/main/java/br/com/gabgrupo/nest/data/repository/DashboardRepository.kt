package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.model.DashboardGroupResponse

interface DashboardRepository {
    suspend fun getDashboard(): Result<DashboardResponse>
    suspend fun getByGuideline(): Result<List<DashboardGroupResponse>>
    suspend fun getByProject(): Result<List<DashboardGroupResponse>>
}