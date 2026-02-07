package com.zanoapps.shared.data.network

import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

/**
 * Base API client for making HTTP requests
 */
class ApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://api.balkanestateai.com/v1"
) {
    suspend inline fun <reified T> get(
        endpoint: String,
        parameters: Map<String, Any?> = emptyMap()
    ): Result<T, DataError.Network> {
        return safeCall {
            httpClient.get("$baseUrl$endpoint") {
                parameters.forEach { (key, value) ->
                    if (value != null) {
                        parameter(key, value.toString())
                    }
                }
            }
        }
    }

    suspend inline fun <reified T, reified R> post(
        endpoint: String,
        body: T
    ): Result<R, DataError.Network> {
        return safeCall {
            httpClient.post("$baseUrl$endpoint") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    suspend inline fun <reified T, reified R> put(
        endpoint: String,
        body: T
    ): Result<R, DataError.Network> {
        return safeCall {
            httpClient.put("$baseUrl$endpoint") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    suspend inline fun <reified T> delete(
        endpoint: String
    ): Result<T, DataError.Network> {
        return safeCall {
            httpClient.delete("$baseUrl$endpoint")
        }
    }

    suspend inline fun <reified T> safeCall(
        execute: () -> HttpResponse
    ): Result<T, DataError.Network> {
        val response = try {
            execute()
        } catch (e: UnresolvedAddressException) {
            return Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(DataError.Network.SERIALIZATION)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            return Result.Error(DataError.Network.UNKNOWN)
        }

        return responseToResult(response)
    }

    suspend inline fun <reified T> responseToResult(
        response: HttpResponse
    ): Result<T, DataError.Network> {
        return when {
            response.status.isSuccess() -> {
                try {
                    Result.Success(response.body<T>())
                } catch (e: SerializationException) {
                    Result.Error(DataError.Network.SERIALIZATION)
                }
            }
            response.status.value == 408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
            response.status.value == 429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
            response.status.value in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
            else -> Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
