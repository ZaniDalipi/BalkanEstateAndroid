package com.zanoapps.auth.domain.model

data class AuthUser(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String = "",
    val avatarUrl: String = "",
    val accountType: AccountType = AccountType.BUYER,
    val isPremium: Boolean = false,
    val isVerified: Boolean = false,
    val token: String = ""
)

enum class AccountType {
    BUYER, SELLER, AGENT
}
