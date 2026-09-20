package br.com.gabgrupo.nest.data.model

data class DashboardGroupResponse(
    val id: String?,
    val projectCount: Long,
    val totalInvestment: Double?,
    val totalExpectedReturn: Double?,
    val totalActualReturn: Double?,
    val totalProductivityGain: Double?
)