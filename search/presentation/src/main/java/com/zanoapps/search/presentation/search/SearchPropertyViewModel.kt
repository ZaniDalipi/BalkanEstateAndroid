package com.zanoapps.search.presentation.search

import android.view.SearchEvent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchPropertyViewModel(
    private val userId: String?
) : ViewModel() {

     var state by mutableStateOf(SearchState())
        private set

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()


    init {

    }

    fun onAction(action: SearchAction) {
        when(action) {
            SearchAction.OnClearFilters -> TODO()
            SearchAction.OnCollapseBottomSheet -> TODO()
            SearchAction.OnCreateListingClick -> TODO()
            is SearchAction.OnDeleteSavedSearch -> TODO()
            SearchAction.OnExpandBottomSheet -> TODO()
            is SearchAction.OnFavoriteToggle -> TODO()
            SearchAction.OnFilterClick -> TODO()
            is SearchAction.OnFiltersApplied -> TODO()
            is SearchAction.OnLoadSavedSearch -> TODO()
            is SearchAction.OnMapMoved -> TODO()
            SearchAction.OnMapTypeToggle -> TODO()
            is SearchAction.OnMarkerClicked -> TODO()
            SearchAction.OnMyLocationClick -> TODO()
            is SearchAction.OnPropertyClicked -> TODO()
            SearchAction.OnRefreshProperties -> TODO()
            is SearchAction.OnSaveSearch -> TODO()
            SearchAction.OnSaveSearchClick -> TODO()
            is SearchAction.OnSearchQueryChanged -> TODO()
            SearchAction.OnSearchSubmit -> TODO()
            is SearchAction.OnSortChanged -> TODO()
            SearchAction.OnViewSavedSearches -> TODO()
        }
    }


    private fun loadProperties(isRefresh: Boolean = false) {
        viewModelScope.launch {
            state = state.copy(
                isLoadingProperties = !isRefresh,
                isRefreshing = isRefresh
            )

        }
    }


}