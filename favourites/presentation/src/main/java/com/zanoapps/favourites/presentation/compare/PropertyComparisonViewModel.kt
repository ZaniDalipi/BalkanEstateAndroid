package com.zanoapps.favourites.presentation.compare

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.repository.PropertyRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PropertyComparisonViewModel(
    private val propertyRepository: PropertyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(PropertyComparisonState())
        private set

    private val eventChannel = Channel<PropertyComparisonEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        val propertyIds = savedStateHandle.get<String>("propertyIds")
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()
        loadProperties(propertyIds)
    }

    fun onAction(action: PropertyComparisonAction) {
        when (action) {
            PropertyComparisonAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyComparisonEvent.NavigateBack)
                }
            }
            is PropertyComparisonAction.OnViewDetailsClick -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyComparisonEvent.NavigateToPropertyDetail(action.propertyId))
                }
            }
        }
    }

    private fun loadProperties(propertyIds: List<String>) {
        if (propertyIds.isEmpty()) {
            state = state.copy(errorMessage = "No properties to compare")
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            val results = propertyIds.map { id ->
                async { propertyRepository.getPropertyById(id) }
            }.awaitAll()

            val properties = mutableListOf<BalkanEstateProperty>()
            var hasError = false
            for (result in results) {
                when (result) {
                    is Result.Success -> properties.add(result.data)
                    is Result.Error -> hasError = true
                }
            }

            state = if (properties.isEmpty()) {
                state.copy(
                    isLoading = false,
                    errorMessage = "Failed to load properties for comparison"
                )
            } else {
                state.copy(
                    properties = properties,
                    isLoading = false,
                    errorMessage = if (hasError && properties.isNotEmpty()) {
                        "Some properties could not be loaded"
                    } else null
                )
            }
        }
    }
}
