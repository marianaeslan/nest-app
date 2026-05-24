package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.model.ProjectRequest
import br.com.gabgrupo.nest.data.model.ProjectResponse
import br.com.gabgrupo.nest.data.model.ProjectSummary
import br.com.gabgrupo.nest.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<ProjectListState>(ProjectListState.Idle)
    val listState: StateFlow<ProjectListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<ProjectDetailState>(ProjectDetailState.Idle)
    val detailState: StateFlow<ProjectDetailState> = _detailState.asStateFlow()

    private val _actionState = MutableStateFlow<ProjectActionState>(ProjectActionState.Idle)
    val actionState: StateFlow<ProjectActionState> = _actionState.asStateFlow()

    fun getAllProjects() {
        viewModelScope.launch {
            _listState.value = ProjectListState.Loading
            val result = projectRepository.getAll()

            result.onSuccess { response ->
                _listState.value = ProjectListState.Success(response)
            }.onFailure { exception ->
                _listState.value = ProjectListState.Error(exception.message ?: "Erro ao buscar projetos.")
            }
        }
    }

    fun getProjectById(id: Long) {
        viewModelScope.launch {
            _detailState.value = ProjectDetailState.Loading
            val result = projectRepository.getById(id)

            result.onSuccess { response ->
                _detailState.value = ProjectDetailState.Success(response)
            }.onFailure { exception ->
                _detailState.value = ProjectDetailState.Error(exception.message ?: "Erro ao buscar projeto.")
            }
        }
    }

    fun createProject(request: ProjectRequest) {
        viewModelScope.launch {
            _actionState.value = ProjectActionState.Loading
            val result = projectRepository.create(request)

            result.onSuccess { response ->
                _actionState.value = ProjectActionState.Success(response)
            }.onFailure { exception ->
                _actionState.value = ProjectActionState.Error(exception.message ?: "Erro ao criar projeto.")
            }
        }
    }

    fun updateProject(id: Long, request: ProjectRequest) {
        viewModelScope.launch {
            _actionState.value = ProjectActionState.Loading
            val result = projectRepository.update(id, request)

            result.onSuccess { response ->
                _actionState.value = ProjectActionState.Success(response)
            }.onFailure { exception ->
                _actionState.value = ProjectActionState.Error(exception.message ?: "Erro ao atualizar projeto.")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = ProjectActionState.Idle
    }
}

sealed class ProjectListState {
    data object Idle : ProjectListState()
    data object Loading : ProjectListState()
    data class Success(val projects: List<ProjectSummary>) : ProjectListState()
    data class Error(val message: String) : ProjectListState()
}

sealed class ProjectDetailState {
    data object Idle : ProjectDetailState()
    data object Loading : ProjectDetailState()
    data class Success(val project: ProjectResponse) : ProjectDetailState()
    data class Error(val message: String) : ProjectDetailState()
}

sealed class ProjectActionState {
    data object Idle : ProjectActionState()
    data object Loading : ProjectActionState()
    data class Success(val project: ProjectResponse) : ProjectActionState()
    data class Error(val message: String) : ProjectActionState()
}