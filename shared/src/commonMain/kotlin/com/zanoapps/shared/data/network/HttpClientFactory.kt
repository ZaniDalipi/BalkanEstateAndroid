package com.zanoapps.shared.data.network

import io.ktor.client.HttpClient

/**
 * Factory for creating platform-specific HTTP clients
 */
expect fun createHttpClient(): HttpClient
