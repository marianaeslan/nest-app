package br.com.gabgrupo.nest.data.remote

import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GuidelineApiService {
    @GET("/api/guidelines")
    suspend fun getAll(): Response<List<GuidelineResponse>>

    @POST("/api/guidelines")
    suspend fun create(@Body request: GuidelineRequest): Response<GuidelineResponse>

    @PUT("/api/guidelines/{id}")
    suspend fun update(@Path("id") id: Long, @Body request: GuidelineRequest): Response<GuidelineResponse>

    @DELETE("/api/guidelines/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}