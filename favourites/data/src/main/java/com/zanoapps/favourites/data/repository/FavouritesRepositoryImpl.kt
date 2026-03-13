package com.zanoapps.favourites.data.repository

import com.zanoapps.core.database.dao.FavoriteDao
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import com.zanoapps.search.data.repository.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavouritesRepository {

    override fun getFavouriteProperties(): Flow<List<BalkanEstateProperty>> {
        return favoriteDao.getFavouriteProperties().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addFavourite(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favoriteDao.addFavourite(
                FavoritePropertyEntity(propertyId = propertyId, userId = "current_user")
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeFavourite(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favoriteDao.removeFavourite(propertyId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun isFavourite(propertyId: String): Boolean {
        return favoriteDao.isFavourite(propertyId)
    }
}
