package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.model.GuidelineHistory

interface GuidelineRepository {
    suspend fun getAll(): Result<List<GuidelineResponse>>
    suspend fun create(request: GuidelineRequest): Result<GuidelineResponse>
    suspend fun update(id: String, request: GuidelineRequest): Result<GuidelineResponse>
    suspend fun delete(id: String): Result<Unit>
    suspend fun getHistory(id: String): Result<List<GuidelineHistory>>
}