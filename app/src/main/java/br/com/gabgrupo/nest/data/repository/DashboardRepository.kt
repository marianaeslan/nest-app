package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.DashboardResponse

interface DashboardRepository {
    suspend fun getDashboard(): Result<DashboardResponse>
}