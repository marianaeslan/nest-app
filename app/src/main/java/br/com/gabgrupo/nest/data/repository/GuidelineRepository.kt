package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse

interface GuidelineRepository {
    suspend fun getAll(): Result<List<GuidelineResponse>>
    suspend fun create(request: GuidelineRequest): Result<GuidelineResponse>
    suspend fun update(id: Long, request: GuidelineRequest): Result<GuidelineResponse>
    suspend fun delete(id: Long): Result<Unit>
}