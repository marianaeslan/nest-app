package br.com.gabgrupo.nest.data.model

data class ProjectSummary(
    val id: Long,
    val title: String,
    val status: String,
    val stage: String,
    val investment: Double?,
    val expectedReturn: Double?,
    val actualReturn: Double?,
    val productivityGain: Double?,
    val startDate: String?,
    val endDate: String?
)

