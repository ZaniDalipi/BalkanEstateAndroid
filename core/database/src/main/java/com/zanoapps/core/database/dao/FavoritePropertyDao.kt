package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.database.entity.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePropertyDao {

    @Query("""
        SELECT p.* FROM properties p
        INNER JOIN favorite_properties f ON p.id = f.propertyId
        WHERE f.userId = :userId
        ORDER BY f.saveAt DESC
    """)
    fun getFavoriteProperties(userId: String): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM favorite_properties WHERE userId = :userId")
    fun getFavorites(userId: String): Flow<List<FavoritePropertyEntity>>

    @Query("SELECT * FROM favorite_properties WHERE userId = :userId AND propertyId = :propertyId")
    suspend fun getFavorite(userId: String, propertyId: String): FavoritePropertyEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_properties WHERE userId = :userId AND propertyId = :propertyId)")
    fun isFavorite(userId: String, propertyId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoritePropertyEntity)

    @Query("DELETE FROM favorite_properties WHERE userId = :userId AND propertyId = :propertyId")
    suspend fun removeFavorite(userId: String, propertyId: String)

    @Query("DELETE FROM favorite_properties WHERE userId = :userId")
    suspend fun removeAllFavorites(userId: String)

    @Query("SELECT COUNT(*) FROM favorite_properties WHERE userId = :userId")
    fun getFavoriteCount(userId: String): Flow<Int>
}
