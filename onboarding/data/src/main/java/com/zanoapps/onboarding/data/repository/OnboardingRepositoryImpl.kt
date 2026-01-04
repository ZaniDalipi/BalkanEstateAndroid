package com.zanoapps.onboarding.data.repository

import com.zanoapps.core.database.dao.OnboardingPreferencesDao
import com.zanoapps.core.database.entity.OnboardingPreferencesEntity
import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent
import com.zanoapps.onboarding.domain.enums.seller.MainGoal
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.domain.enums.seller.SellingTime
import com.zanoapps.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room-based implementation of OnboardingRepository.
 * Persists onboarding preferences locally.
 */
class OnboardingRepositoryImpl(
    private val onboardingPreferencesDao: OnboardingPreferencesDao
) : OnboardingRepository {

    // TODO: Get from auth repository/session
    private val currentUserId = "current_user"

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun saveClientIntent(intent: ClientIntent) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                clientIntent = intent.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getClientIntent(): Flow<ClientIntent?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.clientIntent?.let { ClientIntent.valueOf(it) }
        }
    }

    // Buyer Preferences
    override suspend fun saveLifeSituation(situation: LifeSituation) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                lifeSituation = situation.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun savePropertyIntent(intent: PropertyIntent) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                propertyIntent = intent.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveAmenities(amenities: List<Amenity>) {
        val preferences = getOrCreatePreferences()
        val amenitiesJson = json.encodeToString(amenities.map { it.name })
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                amenities = amenitiesJson,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getLifeSituation(): Flow<LifeSituation?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.lifeSituation?.let { LifeSituation.valueOf(it) }
        }
    }

    override fun getPropertyIntent(): Flow<PropertyIntent?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.propertyIntent?.let { PropertyIntent.valueOf(it) }
        }
    }

    override fun getAmenities(): Flow<List<Amenity>> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.amenities?.let { amenitiesJson ->
                try {
                    val names: List<String> = json.decodeFromString(amenitiesJson)
                    names.mapNotNull { name ->
                        try { Amenity.valueOf(name) } catch (e: Exception) { null }
                    }
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
        }
    }

    // Seller Preferences
    override suspend fun savePropertyTypeSeller(type: PropertyTypeSeller) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                propertyTypeSeller = type.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveSellingTime(time: SellingTime) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                sellingTime = time.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveMainGoal(goal: MainGoal) {
        val preferences = getOrCreatePreferences()
        onboardingPreferencesDao.insertPreferences(
            preferences.copy(
                mainGoal = goal.name,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getPropertyTypeSeller(): Flow<PropertyTypeSeller?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.propertyTypeSeller?.let { PropertyTypeSeller.valueOf(it) }
        }
    }

    override fun getSellingTime(): Flow<SellingTime?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.sellingTime?.let { SellingTime.valueOf(it) }
        }
    }

    override fun getMainGoal(): Flow<MainGoal?> {
        return onboardingPreferencesDao.getPreferences(currentUserId).map { entity ->
            entity?.mainGoal?.let { MainGoal.valueOf(it) }
        }
    }

    override suspend fun markOnboardingCompleted() {
        onboardingPreferencesDao.markOnboardingCompleted(currentUserId)
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return onboardingPreferencesDao.isOnboardingCompleted(currentUserId) ?: false
    }

    override suspend fun clearPreferences() {
        onboardingPreferencesDao.deletePreferences(currentUserId)
    }

    private suspend fun getOrCreatePreferences(): OnboardingPreferencesEntity {
        return onboardingPreferencesDao.getPreferencesOnce(currentUserId)
            ?: OnboardingPreferencesEntity(userId = currentUserId)
    }
}
