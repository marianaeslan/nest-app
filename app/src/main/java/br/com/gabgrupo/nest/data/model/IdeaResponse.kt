package br.com.gabgrupo.nest.data.model

data class IdeaResponse(
    val id: String,
    val title: String,
    val description: String,
    val status: IdeaStatus,
    val priority: Int?,
    val submittedBy: UserSummary,
    val reviewedBy: UserSummary?,
    val reviewedAt: String? = null,
    val guidelineId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
