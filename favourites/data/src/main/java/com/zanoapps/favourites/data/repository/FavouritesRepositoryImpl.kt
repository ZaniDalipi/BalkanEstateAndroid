package com.zanoapps.favourites.data.repository

import com.zanoapps.core.data.mappers.toDomainList
import com.zanoapps.core.database.dao.FavoritePropertyDao
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * Single Source of Truth Implementation for Favourites.
 *
 * Room database is the single source of truth.
 * UI observes Room database through Flow.
 */
class FavouritesRepositoryImpl(
    private val favoritePropertyDao: FavoritePropertyDao
) : FavouritesRepository {

    // TODO: Get from auth repository/session
    private val currentUserId = "current_user"

    override fun getFavouriteProperties(): Flow<List<BalkanEstateProperty>> {
        return favoritePropertyDao.getFavoriteProperties(currentUserId)
            .map { entities -> entities.toDomainList() }
    }

    override suspend fun addToFavourites(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favoritePropertyDao.insertFavorite(
                FavoritePropertyEntity(
                    propertyId = propertyId,
                    userId = currentUserId,
                    saveAt = LocalDateTime.now()
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeFromFavourites(propertyId: String): EmptyResult<DataError.Local> {
        return try {
            favoritePropertyDao.removeFavorite(currentUserId, propertyId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun isFavourite(propertyId: String): Boolean {
        return favoritePropertyDao.getFavorite(currentUserId, propertyId) != null
    }

    override suspend fun getFavouriteIds(): Set<String> {
        return favoritePropertyDao.getFavorites(currentUserId)
            .first()
            .map { it.propertyId }
            .toSet()
    }
}
