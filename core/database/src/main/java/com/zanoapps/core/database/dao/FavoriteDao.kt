package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.database.entity.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("""
        SELECT p.* FROM properties p
        INNER JOIN favorite_properties f ON p.id = f.propertyId
        ORDER BY f.saveAt DESC
    """)
    fun getFavouriteProperties(): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(entity: FavoritePropertyEntity)

    @Query("DELETE FROM favorite_properties WHERE propertyId = :propertyId")
    suspend fun removeFavourite(propertyId: String)

    @Query("SELECT COUNT(*) > 0 FROM favorite_properties WHERE propertyId = :propertyId")
    suspend fun isFavourite(propertyId: String): Boolean

    @Query("SELECT COUNT(*) FROM favorite_properties")
    fun getFavouriteCount(): Flow<Int>
}
