package br.com.gabgrupo.nest.data.model

import java.math.BigDecimal

data class ProjectRequest(
    val title: String,
    val description: String,
    val status: ProjectStatus,
    val stage: ProjectStage,
    val investment: BigDecimal,
    val expectedReturn: BigDecimal,
    val actualReturn: BigDecimal?,
    val productivityGain: BigDecimal?,
    val startDate: String,
    val endDate: String?,
    val idea: Long?
)

