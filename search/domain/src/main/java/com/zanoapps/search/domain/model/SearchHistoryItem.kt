package com.zanoapps.search.domain.model

data class SearchHistoryItem(
    val id: Long = 0,
    val userId: String,
    val query: String,
    val searchType: SearchType = SearchType.TEXT,
    val filtersJson: String? = null,
    val resultsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SearchType {
    TEXT,
    FILTER,
    AI
}
