package br.com.gabgrupo.nest.data.model

data class GuidelineHistory(
    val id: String,
    val guidelineId: String,
    val date: String,
    val category: String?,
    val campaign: String?,
    val contentSnapshot: String
)