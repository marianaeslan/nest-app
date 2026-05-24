package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = DashboardState.Loading
            val result = dashboardRepository.getDashboard()

            result.onSuccess { response ->
                _state.value = DashboardState.Success(response)
            }.onFailure { exception ->
                _state.value = DashboardState.Error(exception.message ?: "Erro ao carregar o dashboard.")
            }
        }
    }
}

sealed class DashboardState {
    data object Idle : DashboardState()
    data object Loading : DashboardState()
    data class Success(val data: DashboardResponse) : DashboardState()
    data class Error(val message: String) : DashboardState()
}