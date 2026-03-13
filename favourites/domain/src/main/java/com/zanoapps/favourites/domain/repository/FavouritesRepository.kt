package com.zanoapps.favourites.domain.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavouriteProperties(): Flow<List<BalkanEstateProperty>>
    suspend fun addFavourite(propertyId: String): EmptyResult<DataError.Local>
    suspend fun removeFavourite(propertyId: String): EmptyResult<DataError.Local>
    suspend fun isFavourite(propertyId: String): Boolean
}
