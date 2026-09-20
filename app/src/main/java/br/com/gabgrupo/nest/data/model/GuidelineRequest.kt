package br.com.gabgrupo.nest.data.model

data class GuidelineRequest(
    val title: String,
    val content: String,
    val category: String? = null,
    val campaign: String? = null
)

