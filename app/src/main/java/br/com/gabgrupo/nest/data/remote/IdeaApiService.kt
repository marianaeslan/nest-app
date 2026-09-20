package br.com.gabgrupo.nest.data.remote

import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface IdeaApiService {
    @GET("/api/ideas")
    suspend fun getAll(): Response<List<IdeaResponse>>

    @GET("/api/ideas/my")
    suspend fun getMyIdeas(): Response<List<IdeaResponse>>

    @GET("/api/ideas/overview")
    suspend fun getOverview(): Response<List<IdeaResponse>>

    @POST("/api/ideas")
    suspend fun create(@Body request: IdeaRequest): Response<IdeaResponse>

    @PUT("/api/ideas/{id}")
    suspend fun update(@Path("id") id: String, @Body request: IdeaRequest): Response<IdeaResponse>

    @DELETE("/api/ideas/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

    @PATCH("/api/ideas/{id}/review")
    suspend fun review(@Path("id") id: String, @Body request: IdeaReviewRequest): Response<IdeaResponse>
}