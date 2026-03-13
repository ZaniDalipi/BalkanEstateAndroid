package com.zanoapps.auth.presentation.register

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val agreeToTerms: Boolean = false,
    val accountType: AccountType = AccountType.BUYER,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class AccountType(val displayName: String) {
    BUYER("Buyer/Renter"),
    SELLER("Seller/Owner"),
    AGENT("Real Estate Agent")
}
