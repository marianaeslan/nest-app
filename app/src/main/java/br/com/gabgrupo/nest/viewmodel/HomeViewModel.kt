package br.com.gabgrupo.nest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gabgrupo.nest.data.local.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        loadUserName()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val fullName = tokenDataStore.getName() ?: "Usuário"
            _userName.value = fullName.split(" ").firstOrNull() ?: fullName
        }
    }
}
