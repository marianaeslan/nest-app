package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.repository.GuidelineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuidelineViewModel @Inject constructor(
    private val guidelineRepository: GuidelineRepository
) : ViewModel() {

    private val _listState = MutableStateFlow<GuidelineListState>(GuidelineListState.Idle)
    val listState: StateFlow<GuidelineListState> = _listState.asStateFlow()

    private val _actionState = MutableStateFlow<GuidelineActionState>(GuidelineActionState.Idle)
    val actionState: StateFlow<GuidelineActionState> = _actionState.asStateFlow()

    fun getAllGuidelines() {
        viewModelScope.launch {
            _listState.value = GuidelineListState.Loading
            val result = guidelineRepository.getAll()

            result.onSuccess { response ->
                _listState.value = GuidelineListState.Success(response)
            }.onFailure { exception ->
                _listState.value = GuidelineListState.Error(exception.message ?: "Erro ao buscar diretrizes.")
            }
        }
    }

    fun createGuideline(request: GuidelineRequest) {
        viewModelScope.launch {
            _actionState.value = GuidelineActionState.Loading
            val result = guidelineRepository.create(request)

            result.onSuccess {
                _actionState.value = GuidelineActionState.Success
                getAllGuidelines()
            }.onFailure { exception ->
                _actionState.value = GuidelineActionState.Error(exception.message ?: "Erro ao criar diretriz.")
            }
        }
    }

    fun updateGuideline(id: Long, request: GuidelineRequest) {
        viewModelScope.launch {
            _actionState.value = GuidelineActionState.Loading
            val result = guidelineRepository.update(id, request)

            result.onSuccess {
                _actionState.value = GuidelineActionState.Success
                getAllGuidelines()
            }.onFailure { exception ->
                _actionState.value = GuidelineActionState.Error(exception.message ?: "Erro ao atualizar diretriz.")
            }
        }
    }

    fun deleteGuideline(id: Long) {
        viewModelScope.launch {
            _actionState.value = GuidelineActionState.Loading
            val result = guidelineRepository.delete(id)

            result.onSuccess {
                _actionState.value = GuidelineActionState.Success
                getAllGuidelines()
            }.onFailure { exception ->
                _actionState.value = GuidelineActionState.Error(exception.message ?: "Erro ao deletar diretriz.")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = GuidelineActionState.Idle
    }
}

sealed class GuidelineListState {
    data object Idle : GuidelineListState()
    data object Loading : GuidelineListState()
    data class Success(val guidelines: List<GuidelineResponse>) : GuidelineListState()
    data class Error(val message: String) : GuidelineListState()
}

sealed class GuidelineActionState {
    data object Idle : GuidelineActionState()
    data object Loading : GuidelineActionState()
    data object Success : GuidelineActionState()
    data class Error(val message: String) : GuidelineActionState()
}