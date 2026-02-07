package com.zanoapps.shared.util

/**
 * A generic sealed class that represents either a successful result with data
 * or an error with error information
 */
sealed class Result<out D, out E: Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Error<out E: com.zanoapps.shared.util.Error>(val error: E) : Result<Nothing, E>()
}

/**
 * Marker interface for error types
 */
interface Error

/**
 * Common data errors that can occur across the app
 */
sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}

/**
 * Authentication errors
 */
sealed interface AuthError : Error {
    data object InvalidCredentials : AuthError
    data object UserNotFound : AuthError
    data object EmailAlreadyExists : AuthError
    data object WeakPassword : AuthError
    data object InvalidEmail : AuthError
    data object TokenExpired : AuthError
    data object Unauthorized : AuthError
    data class Unknown(val message: String? = null) : AuthError
}

/**
 * Extension functions for Result
 */
inline fun <T, E : Error, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(error)
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}

fun <T, E : Error> Result<T, E>.getOrNull(): T? {
    return when (this) {
        is Result.Success -> data
        is Result.Error -> null
    }
}

fun <T, E : Error> Result<T, E>.getOrDefault(default: T): T {
    return when (this) {
        is Result.Success -> data
        is Result.Error -> default
    }
}

suspend fun <T, E : Error> Result<T, E>.getOrThrow(): T {
    return when (this) {
        is Result.Success -> data
        is Result.Error -> throw Exception("Result error: $error")
    }
}

/**
 * Converts a nullable value to a Result
 */
fun <T> T?.toResult(error: Error = DataError.Local.NOT_FOUND): Result<T, Error> {
    return if (this != null) {
        Result.Success(this)
    } else {
        Result.Error(error)
    }
}
