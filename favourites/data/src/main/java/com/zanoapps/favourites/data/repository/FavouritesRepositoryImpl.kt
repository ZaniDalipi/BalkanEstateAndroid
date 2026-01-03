package com.zanoapps.favourites.data.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl : FavouritesRepository {

    // In-memory storage for now - would be replaced with Room database
    private val favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    private val favouriteProperties = MutableStateFlow<List<BalkanEstateProperty>>(emptyList())

    override fun getFavouriteProperties(): Flow<List<BalkanEstateProperty>> {
        return favouriteProperties
    }

    override suspend fun addToFavourites(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favouriteIds.value = favouriteIds.value + propertyId
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeFromFavourites(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favouriteIds.value = favouriteIds.value - propertyId
            favouriteProperties.value = favouriteProperties.value.filter { it.id != propertyId }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun isFavourite(propertyId: String): Boolean {
        return favouriteIds.value.contains(propertyId)
    }

    override suspend fun getFavouriteIds(): Set<String> {
        return favouriteIds.value
    }
}
