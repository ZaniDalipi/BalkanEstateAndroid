package com.zanoapps.search.presentation.search

import com.zanoapps.presentation.ui.UiText


sealed interface SearchEvent {
    
    data class Error(val message: UiText) : SearchEvent
    data class Success(val message: UiText) : SearchEvent
    
    data class NavigateToPropertyDetails(val propertyId: String) : SearchEvent
    data object NavigateToFilters : SearchEvent
    data object NavigateToSavedSearches : SearchEvent
    data object NavigateToCreateListing : SearchEvent
    data object NavigateToSavedProperties : SearchEvent
    data object NavigateToTopAgents : SearchEvent
    data object NavigateToAgencies : SearchEvent
    data object NavigateToSubscription : SearchEvent
    data object NavigateToInbox : SearchEvent
    data object NavigateToProfile : SearchEvent
    
    data object LoginRequired : SearchEvent
    
    data object SearchSaved : SearchEvent
    data object SearchDeleted : SearchEvent
    data object SubscribedToNewsletter : SearchEvent
}
