package com.zanoapps.search.data.mappers

import com.zanoapps.core.database.entity.SearchHistoryEntity
import com.zanoapps.search.domain.model.SearchHistoryItem
import com.zanoapps.search.domain.model.SearchType

fun SearchHistoryEntity.toDomain(): SearchHistoryItem {
    return SearchHistoryItem(
        id = id,
        userId = userId,
        query = query,
        searchType = when (searchType.lowercase()) {
            "text" -> SearchType.TEXT
            "filter" -> SearchType.FILTER
            "ai" -> SearchType.AI
            else -> SearchType.TEXT
        },
        filtersJson = filtersJson,
        resultsCount = resultsCount,
        createdAt = createdAt
    )
}

fun SearchHistoryItem.toEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        id = id,
        userId = userId,
        query = query,
        searchType = searchType.name.lowercase(),
        filtersJson = filtersJson,
        resultsCount = resultsCount,
        createdAt = createdAt
    )
}

fun List<SearchHistoryEntity>.toDomainList(): List<SearchHistoryItem> = map { it.toDomain() }
