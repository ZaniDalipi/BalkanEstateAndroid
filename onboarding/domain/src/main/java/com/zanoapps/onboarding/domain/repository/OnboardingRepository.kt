package com.zanoapps.onboarding.domain.repository

import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent
import com.zanoapps.onboarding.domain.enums.seller.MainGoal
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.domain.enums.seller.SellingTime
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing onboarding preferences.
 * Uses Room database as single source of truth.
 */
interface OnboardingRepository {

    // Client Intent
    suspend fun saveClientIntent(intent: ClientIntent)
    fun getClientIntent(): Flow<ClientIntent?>

    // Buyer Preferences
    suspend fun saveLifeSituation(situation: LifeSituation)
    suspend fun savePropertyIntent(intent: PropertyIntent)
    suspend fun saveAmenities(amenities: List<Amenity>)

    fun getLifeSituation(): Flow<LifeSituation?>
    fun getPropertyIntent(): Flow<PropertyIntent?>
    fun getAmenities(): Flow<List<Amenity>>

    // Seller Preferences
    suspend fun savePropertyTypeSeller(type: PropertyTypeSeller)
    suspend fun saveSellingTime(time: SellingTime)
    suspend fun saveMainGoal(goal: MainGoal)

    fun getPropertyTypeSeller(): Flow<PropertyTypeSeller?>
    fun getSellingTime(): Flow<SellingTime?>
    fun getMainGoal(): Flow<MainGoal?>

    // Onboarding Status
    suspend fun markOnboardingCompleted()
    suspend fun isOnboardingCompleted(): Boolean

    // Clear all preferences
    suspend fun clearPreferences()
}
