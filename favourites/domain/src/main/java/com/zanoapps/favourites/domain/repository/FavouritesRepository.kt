package com.zanoapps.favourites.domain.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavouriteProperties(): Flow<List<BalkanEstateProperty>>
    suspend fun addToFavourites(propertyId: String): EmptyResult<DataError.Local>
    suspend fun removeFromFavourites(propertyId: String): EmptyResult<DataError.Local>
    suspend fun isFavourite(propertyId: String): Boolean
    suspend fun getFavouriteIds(): Set<String>
}
