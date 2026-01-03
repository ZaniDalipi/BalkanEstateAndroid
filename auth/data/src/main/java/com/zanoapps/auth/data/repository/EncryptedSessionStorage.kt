package com.zanoapps.auth.data.repository

import android.content.SharedPreferences
import com.zanoapps.auth.domain.model.AuthInfo
import com.zanoapps.auth.domain.repository.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
) : SessionStorage {

    override suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null) ?: return@withContext null
            Json.decodeFromString<AuthInfoSerializable>(json).toAuthInfo()
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(Dispatchers.IO) {
            if (info == null) {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
                return@withContext
            }
            val json = Json.encodeToString(info.toSerializable())
            sharedPreferences.edit().putString(KEY_AUTH_INFO, json).apply()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "auth_info"
    }
}

@kotlinx.serialization.Serializable
private data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)

private fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}

private fun AuthInfo.toSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}
