package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaReviewRequest
import br.com.gabgrupo.nest.data.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdeaViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<IdeaListState>(IdeaListState.Idle)
    val listState: StateFlow<IdeaListState> = _listState.asStateFlow()

    private val _actionState = MutableStateFlow<IdeaActionState>(IdeaActionState.Idle)
    val actionState: StateFlow<IdeaActionState> = _actionState.asStateFlow()

    fun getAllIdeas() {
        viewModelScope.launch {
            _listState.value = IdeaListState.Loading
            val result = ideaRepository.getAll()

            result.onSuccess { response ->
                _listState.value = IdeaListState.Success(response)
            }.onFailure { exception ->
                _listState.value = IdeaListState.Error(exception.message ?: "Erro ao buscar ideias.")
            }
        }
    }

    fun getMyIdeas() {
        viewModelScope.launch {
            _listState.value = IdeaListState.Loading
            val result = ideaRepository.getMyIdeas()

            result.onSuccess { response ->
                _listState.value = IdeaListState.Success(response)
            }.onFailure { exception ->
                _listState.value = IdeaListState.Error(exception.message ?: "Erro ao buscar suas ideias.")
            }
        }
    }

    fun createIdea(request: IdeaRequest) {
        viewModelScope.launch {
            _actionState.value = IdeaActionState.Loading
            val result = ideaRepository.create(request)

            result.onSuccess { response ->
                _actionState.value = IdeaActionState.Success(response)
            }.onFailure { exception ->
                _actionState.value = IdeaActionState.Error(exception.message ?: "Erro ao criar ideia.")
            }
        }
    }

    fun reviewIdea(id: Long, request: IdeaReviewRequest) {
        viewModelScope.launch {
            _actionState.value = IdeaActionState.Loading
            val result = ideaRepository.review(id, request)

            result.onSuccess { response ->
                _actionState.value = IdeaActionState.Success(response)
            }.onFailure { exception ->
                _actionState.value = IdeaActionState.Error(exception.message ?: "Erro ao revisar ideia.")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = IdeaActionState.Idle
    }
}

sealed class IdeaListState {
    data object Idle : IdeaListState()
    data object Loading : IdeaListState()
    data class Success(val ideas: List<IdeaResponse>) : IdeaListState()
    data class Error(val message: String) : IdeaListState()
}

sealed class IdeaActionState {
    data object Idle : IdeaActionState()
    data object Loading : IdeaActionState()
    data class Success(val idea: IdeaResponse) : IdeaActionState()
    data class Error(val message: String) : IdeaActionState()
}