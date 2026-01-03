package com.zanoapps.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val profileImageUrl: String?,
    val isAgent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
