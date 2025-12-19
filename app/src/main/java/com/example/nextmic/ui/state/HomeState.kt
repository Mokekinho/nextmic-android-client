package com.example.nextmic.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class HomeState(
    val serverIp: String = "",
    //val microphoneAccess : Boolean = false
)


sealed class HomeEvent{
    data class ServerIpChanged(val serverIp: String): HomeEvent()
    //data class MicrophoneAccessChanged(val microphoneAccess: Boolean): HomeEvent()
}

class HomeViewModel(): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ServerIpChanged -> _state.update { it.copy(serverIp = event.serverIp) }
            //is HomeEvent.MicrophoneAccessChanged -> _state.update { it.copy(microphoneAccess = event.microphoneAccess) }
        }
    }
}