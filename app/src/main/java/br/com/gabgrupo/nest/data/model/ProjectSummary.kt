package br.com.gabgrupo.nest.data.model

import java.math.BigDecimal

data class ProjectSummary(
    val id: Long,
    val title: String,
    val status: ProjectStatus,
    val stage: ProjectStage,
    val investment: BigDecimal,
    val endDate: String?,
    val teamName: String,
    val teamSize: Int
)

