package com.zanoapps.auth.domain.model

sealed interface AuthError {
    data object InvalidCredentials : AuthError
    data object EmailAlreadyExists : AuthError
    data object WeakPassword : AuthError
    data object NetworkError : AuthError
    data object Unknown : AuthError
}
