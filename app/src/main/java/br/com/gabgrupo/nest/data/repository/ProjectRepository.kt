package br.com.gabgrupo.nest.data.repository

import br.com.gabgrupo.nest.data.model.ProjectRequest
import br.com.gabgrupo.nest.data.model.ProjectResponse
import br.com.gabgrupo.nest.data.model.ProjectSummary

interface ProjectRepository {
    suspend fun getAll(): Result<List<ProjectSummary>>
    suspend fun getById(id: Long): Result<ProjectResponse>
    suspend fun create(request: ProjectRequest): Result<ProjectResponse>
    suspend fun update(id: Long, request: ProjectRequest): Result<ProjectResponse>
}