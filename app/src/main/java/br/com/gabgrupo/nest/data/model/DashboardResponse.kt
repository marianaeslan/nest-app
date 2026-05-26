package br.com.gabgrupo.nest.data.model

data class DashboardResponse(
    val totalRoi: Double,
    val totalSavings: Double,
    val completedProjects: Long,
    val ideasImplemented: Long,
    val projects: List<ProjectSummary>
)

