package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zanoapps.core.database.entity.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Query("SELECT * FROM properties ORDER BY createdAt DESC")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: String): Flow<PropertyEntity?>

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    suspend fun getPropertyByIdOnce(propertyId: String): PropertyEntity?

    @Query("""
        SELECT * FROM properties
        WHERE (:listingType IS NULL OR listingType = :listingType)
        AND (:propertyType IS NULL OR propertyType = :propertyType)
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        AND (:minBedrooms IS NULL OR bedrooms >= :minBedrooms)
        AND (:city IS NULL OR city LIKE '%' || :city || '%')
        ORDER BY createdAt DESC
    """)
    fun searchProperties(
        listingType: String? = null,
        propertyType: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minBedrooms: Int? = null,
        city: String? = null
    ): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE isFeatured = 1 ORDER BY createdAt DESC LIMIT :limit")
    fun getFeaturedProperties(limit: Int = 10): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE city = :city ORDER BY createdAt DESC")
    fun getPropertiesByCity(city: String): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE agentId = :agentId ORDER BY createdAt DESC")
    fun getPropertiesByAgent(agentId: String): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperties(properties: List<PropertyEntity>)

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @Query("DELETE FROM properties WHERE id = :propertyId")
    suspend fun deleteProperty(propertyId: String)

    @Query("DELETE FROM properties")
    suspend fun deleteAllProperties()

    @Query("SELECT COUNT(*) FROM properties")
    suspend fun getPropertyCount(): Int
}
