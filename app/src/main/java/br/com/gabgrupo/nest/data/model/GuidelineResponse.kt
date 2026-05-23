package br.com.gabgrupo.nest.data.model

data class GuidelineResponse(
    val id: Long,
    val title: String,
    val content: String,
    val active: Boolean,
    val createdBy: UserSummary,
    val createdAt: String
)

