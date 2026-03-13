package com.zanoapps.search.presentation.filter

import com.zanoapps.search.domain.model.SearchFilters

sealed interface FilterEvent {
    data class FiltersApplied(val filters: SearchFilters) : FilterEvent
    data object NavigateBack : FilterEvent
}
