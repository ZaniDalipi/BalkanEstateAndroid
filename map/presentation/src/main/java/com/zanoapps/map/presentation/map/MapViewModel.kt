package com.zanoapps.map.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.map.domain.model.MapRegion
import com.zanoapps.map.domain.repository.MapRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    var state by mutableStateOf(MapState())
        private set

    private val eventChannel = Channel<MapEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadProperties()
    }

    fun onAction(action: MapAction) {
        when (action) {
            MapAction.OnLoadProperties -> loadProperties()
            is MapAction.OnRegionChanged -> {
                state = state.copy(region = action.region)
                loadProperties()
            }
            is MapAction.OnPropertySelected -> {
                viewModelScope.launch {
                    val property = mapRepository.getPropertyById(action.propertyId)
                    state = state.copy(selectedProperty = property)
                }
            }
            MapAction.OnDismissPropertyCard -> {
                state = state.copy(selectedProperty = null)
            }
        }
    }

    private fun loadProperties() {
        state = state.copy(isLoading = true)
        mapRepository.getPropertiesInRegion(state.region)
            .onEach { properties ->
                state = state.copy(
                    properties = properties,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }
}
