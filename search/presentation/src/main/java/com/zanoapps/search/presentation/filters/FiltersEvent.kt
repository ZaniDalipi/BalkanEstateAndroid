package com.zanoapps.search.presentation.filters

sealed interface FiltersEvent {
    data class FiltersApplied(val filters: FiltersState) : FiltersEvent
    data object FiltersReset : FiltersEvent
    data object NavigateBack : FiltersEvent
}
