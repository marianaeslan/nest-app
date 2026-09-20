package br.com.gabgrupo.nest.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object SessionEvents {
    private val _expired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val expired: SharedFlow<Unit> = _expired

    fun notifyExpired() {
        _expired.tryEmit(Unit)
    }
}
