package com.zanoapps.property_details.presentation.listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.search.domain.repository.PropertyRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyListingsViewModel(
    private val propertyRepository: PropertyRepository
) : ViewModel() {

    var state by mutableStateOf(MyListingsState())
        private set

    private val eventChannel = Channel<MyListingsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadListings()
    }

    fun onAction(action: MyListingsAction) {
        when (action) {
            is MyListingsAction.OnTabSelected -> {
                state = state.copy(selectedTab = action.tab)
            }
            is MyListingsAction.OnEditListing -> {
                viewModelScope.launch {
                    eventChannel.send(MyListingsEvent.NavigateToEditListing(action.listingId))
                }
            }
            is MyListingsAction.OnDeleteListing -> {
                deleteListing(action.listingId)
            }
            MyListingsAction.OnAddNewListing -> {
                viewModelScope.launch {
                    eventChannel.send(MyListingsEvent.NavigateToCreateListing)
                }
            }
            MyListingsAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(MyListingsEvent.NavigateBack)
                }
            }
            MyListingsAction.OnRefresh -> loadListings()
        }
    }

    private fun loadListings() {
        state = state.copy(isLoading = true)
        propertyRepository.getProperties()
            .onEach { properties ->
                val myListings = properties.mapIndexed { index, property ->
                    MyListing(
                        property = property,
                        status = when {
                            index % 5 == 0 -> ListingStatus.SOLD
                            index % 3 == 0 -> ListingStatus.PENDING
                            else -> ListingStatus.ACTIVE
                        },
                        viewsCount = (50..500).random(),
                        inquiriesCount = (2..30).random()
                    )
                }
                state = state.copy(
                    listings = myListings,
                    isLoading = false,
                    errorMessage = null
                )
            }
            .launchIn(viewModelScope)
    }

    private fun deleteListing(listingId: String) {
        state = state.copy(
            listings = state.listings.filter { it.property.id != listingId }
        )
        viewModelScope.launch {
            eventChannel.send(MyListingsEvent.ListingDeleted)
        }
    }
}

sealed interface MyListingsAction {
    data class OnTabSelected(val tab: ListingTab) : MyListingsAction
    data class OnEditListing(val listingId: String) : MyListingsAction
    data class OnDeleteListing(val listingId: String) : MyListingsAction
    data object OnAddNewListing : MyListingsAction
    data object OnBackClick : MyListingsAction
    data object OnRefresh : MyListingsAction
}

sealed interface MyListingsEvent {
    data class NavigateToEditListing(val listingId: String) : MyListingsEvent
    data object NavigateToCreateListing : MyListingsEvent
    data object NavigateBack : MyListingsEvent
    data object ListingDeleted : MyListingsEvent
}
