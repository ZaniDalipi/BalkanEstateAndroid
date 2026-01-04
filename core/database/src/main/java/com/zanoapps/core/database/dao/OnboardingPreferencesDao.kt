package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zanoapps.core.database.entity.OnboardingPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingPreferencesDao {

    @Query("SELECT * FROM onboarding_preferences WHERE userId = :userId")
    fun getPreferences(userId: String): Flow<OnboardingPreferencesEntity?>

    @Query("SELECT * FROM onboarding_preferences WHERE userId = :userId")
    suspend fun getPreferencesOnce(userId: String): OnboardingPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: OnboardingPreferencesEntity)

    @Update
    suspend fun updatePreferences(preferences: OnboardingPreferencesEntity)

    @Query("UPDATE onboarding_preferences SET isOnboardingCompleted = 1, updatedAt = :timestamp WHERE userId = :userId")
    suspend fun markOnboardingCompleted(userId: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM onboarding_preferences WHERE userId = :userId")
    suspend fun deletePreferences(userId: String)

    @Query("SELECT isOnboardingCompleted FROM onboarding_preferences WHERE userId = :userId")
    suspend fun isOnboardingCompleted(userId: String): Boolean?
}
