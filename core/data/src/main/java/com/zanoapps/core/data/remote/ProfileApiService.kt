package com.zanoapps.core.data.remote

import com.zanoapps.core.data.networking.safeCall
import com.zanoapps.core.data.remote.dto.ProfileResponse
import com.zanoapps.core.data.remote.dto.UpdateProfileRequest
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody

class ProfileApiService(
    private val httpClient: HttpClient
) {
    suspend fun getProfile(userId: String): Result<ProfileResponse, DataError.Network> {
        return safeCall {
            httpClient.get("api/users/$userId")
        }
    }

    suspend fun updateProfile(
        userId: String,
        request: UpdateProfileRequest
    ): Result<ProfileResponse, DataError.Network> {
        return safeCall {
            httpClient.patch("api/users/$userId") {
                setBody(request)
            }
        }
    }

    suspend fun updateProfileImage(
        userId: String,
        imageUrl: String
    ): Result<ProfileResponse, DataError.Network> {
        return safeCall {
            httpClient.patch("api/users/$userId/image") {
                setBody(mapOf("profileImageUrl" to imageUrl))
            }
        }
    }

    suspend fun deleteAccount(userId: String): EmptyResult<DataError.Network> {
        return safeCall {
            httpClient.delete("api/users/$userId")
        }
    }
}
