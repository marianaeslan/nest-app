package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.local.TokenDataStore
import br.com.gabgrupo.nest.data.model.DashboardResponse
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderViewModel @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userRole = MutableStateFlow(UserRole.LEADER)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _dashboard = MutableStateFlow<DashboardResponse?>(null)
    val dashboard: StateFlow<DashboardResponse?> = _dashboard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUserData()
        loadDashboard()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val fullName = tokenDataStore.getName() ?: ""
            _userName.value = fullName.split(" ").firstOrNull() ?: fullName

            _userRole.value = tokenDataStore.getRole()
                ?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }
                ?: UserRole.LEADER
        }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            dashboardRepository.getDashboard()
                .onSuccess { _dashboard.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
}