package br.com.gabgrupo.nest.data.remote

import br.com.gabgrupo.nest.data.model.ProjectRequest
import br.com.gabgrupo.nest.data.model.ProjectResponse
import br.com.gabgrupo.nest.data.model.ProjectSummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectApiService {
    @GET("/api/projects")
    suspend fun getAll(): Response<List<ProjectSummary>>

    @GET("/api/projects/{id}")
    suspend fun getById(@Path("id") id: Long): Response<ProjectResponse>

    @POST("/api/projects")
    suspend fun create(@Body request: ProjectRequest): Response<ProjectResponse>

    @PUT("/api/projects/{id}")
    suspend fun update(@Path("id") id: Long, @Body request: ProjectRequest): Response<ProjectResponse>
}