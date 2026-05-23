package br.com.gabgrupo.nest.data.model

data class IdeaReviewRequest(
    val status: IdeaStatus,
    val priority: Int
)

