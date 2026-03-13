package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY createdAt DESC")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: String): PropertyEntity?

    @Query("SELECT * FROM properties WHERE isFeatured = 1")
    suspend fun getFeaturedProperties(): List<PropertyEntity>

    @Query("SELECT * FROM properties WHERE title LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    suspend fun searchProperties(query: String): List<PropertyEntity>

    @Query("SELECT * FROM properties WHERE city = :city AND id != :excludeId LIMIT 5")
    suspend fun getSimilarProperties(city: String, excludeId: String): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(properties: List<PropertyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: PropertyEntity)

    @Query("DELETE FROM properties WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM properties")
    suspend fun deleteAll()
}
