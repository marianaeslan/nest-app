package br.com.gabgrupo.nest.data.model

data class IdeaResponse(
    val id: Long,
    val title: String,
    val description: String,
    val status: IdeaStatus,
    val priority: Int?,
    val submittedBy: UserSummary,
    val reviewedBy: UserSummary?,
    val createdAt: String
)

