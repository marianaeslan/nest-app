package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.local.TokenDataStore
import br.com.gabgrupo.nest.data.model.AuthRequest
import br.com.gabgrupo.nest.data.model.CreateUserRequest
import br.com.gabgrupo.nest.data.model.UserRole
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

    private val _userCreationState = MutableStateFlow<UserCreationState>(UserCreationState.Idle)
    val userCreationState: StateFlow<UserCreationState> = _userCreationState.asStateFlow()

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
            }.onFailure { error ->
                _state.value = AuthState.Error(error.message ?: "Erro ao realizar login.")
            }
        }
    }

    fun createUser(name: String, email: String, role: UserRole) {
        viewModelScope.launch {
            _userCreationState.value = UserCreationState.Loading
            authRepository.createUser(CreateUserRequest(name.trim(), email.trim(), role))
                .onSuccess { _userCreationState.value = UserCreationState.Success(it) }
                .onFailure { _userCreationState.value = UserCreationState.Error(it.message ?: "Erro ao criar colaborador.") }
        }
    }

    fun resetUserCreationState() {
        _userCreationState.value = UserCreationState.Idle
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            tokenDataStore.clearAll()
            onComplete()
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val role: String, val name: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class UserCreationState {
    data object Idle : UserCreationState()
    data object Loading : UserCreationState()
    data class Success(val user: br.com.gabgrupo.nest.data.model.AuthResponse) : UserCreationState()
    data class Error(val message: String) : UserCreationState()
}
