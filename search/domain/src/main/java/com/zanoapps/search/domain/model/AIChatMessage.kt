package com.zanoapps.search.domain.model

data class AIChatMessage(
    val id: String,
    val sessionId: String,
    val userId: String,
    val content: String,
    val isFromUser: Boolean,
    val relatedPropertyIds: List<String> = emptyList(),
    val filtersApplied: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
