package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaReviewRequest

interface IdeaRepository {
    suspend fun getAll(): Result<List<IdeaResponse>>
    suspend fun getMyIdeas(): Result<List<IdeaResponse>>
    suspend fun getOverview(): Result<List<IdeaResponse>>
    suspend fun create(request: IdeaRequest): Result<IdeaResponse>
    suspend fun update(id: String, request: IdeaRequest): Result<IdeaResponse>
    suspend fun delete(id: String): Result<Unit>
    suspend fun review(id: String, request: IdeaReviewRequest): Result<IdeaResponse>
}