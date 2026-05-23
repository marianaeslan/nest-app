package br.com.gabgrupo.nest.data.model

import java.math.BigDecimal

data class DashboardResponse(
    val roiTotal: Double,
    val savingsGenerated: BigDecimal,
    val completedProjects: Int,
    val implementedIdeas: Int,
    val impactByCategory: Map<String, Double>,
    val roiEvolution: List<RoiEvolutionEntry>
)

data class RoiEvolutionEntry(
    val period: String,
    val roi: Double
)

