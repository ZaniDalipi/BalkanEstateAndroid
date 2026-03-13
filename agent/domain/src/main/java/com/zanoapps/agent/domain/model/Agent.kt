package com.zanoapps.agent.domain.model

data class Agent(
    val id: String,
    val name: String,
    val avatarUrl: String = "",
    val agency: String = "",
    val specialization: String = "",
    val location: String = "",
    val phone: String = "",
    val email: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val listingsCount: Int = 0,
    val soldCount: Int = 0,
    val yearsExperience: Int = 0,
    val languages: List<String> = emptyList(),
    val isVerified: Boolean = false,
    val isPremium: Boolean = false,
    val bio: String = ""
)

data class Agency(
    val id: String,
    val name: String,
    val logoUrl: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val agentsCount: Int = 0,
    val listingsCount: Int = 0,
    val description: String = "",
    val isVerified: Boolean = false
)
