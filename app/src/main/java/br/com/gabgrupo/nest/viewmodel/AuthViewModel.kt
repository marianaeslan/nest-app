package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.local.TokenDataStore
import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            tokenDataStore.saveToken("mock-token")
//            tokenDataStore.saveRole("LEADER")
//            tokenDataStore.saveUserId(1L)
//            tokenDataStore.saveName("Marcola beicola")
//            _state.value = AuthState.Success(role = "LEADER", name = "Líder Teste")
//
//        }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthState.Error("Informe e-mail e senha.")
            return
        }

        viewModelScope.launch {
            _state.value = AuthState.Loading

            val result = authRepository.login(AuthRequest(email.trim(), password))

            result.onSuccess { response ->
                tokenDataStore.saveToken(response.token)
                tokenDataStore.saveRole(response.role.name)
                tokenDataStore.saveUserId(response.userId)
                tokenDataStore.saveName(response.name)
                _state.value = AuthState.Success(response.role.name, response.name)
            }
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val role: String, val name: String) : AuthState()
    data class Error(val message: String) : AuthState()
}