package com.zanoapps.agent.domain.model

data class Agent(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val imageUrl: String?,
    val bio: String?,
    val agency: String?,
    val licenseNumber: String?,
    val specializations: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val listingsCount: Int = 0,
    val soldCount: Int = 0,
    val yearsOfExperience: Int = 0,
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val initials: String
        get() = name.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
}
