package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val query: String,
    val searchType: String, // "text", "filter", "ai"
    val filtersJson: String?, // JSON representation of applied filters
    val resultsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
