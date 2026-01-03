package com.zanoapps.auth.data.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.zanoapps.auth.data.repository.AuthRepositoryImpl
import com.zanoapps.auth.data.repository.EncryptedSessionStorage
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.auth.domain.repository.SessionStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<SessionStorage> {
        val context = androidContext()
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        EncryptedSessionStorage(sharedPreferences)
    }

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}
