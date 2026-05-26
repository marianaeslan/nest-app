package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.local.TokenDataStore
import br.com.gabgrupo.nest.data.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderViewModel @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userRole = MutableStateFlow(UserRole.LEADER)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    init {
        viewModelScope.launch {
            _userName.value = tokenDataStore.getName() ?: ""
            _userRole.value = tokenDataStore.getRole()
                ?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }
                ?: UserRole.LEADER
        }
    }
}