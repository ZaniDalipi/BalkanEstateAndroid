package com.zanoapps.onboarding.presentation.buyer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent
import com.zanoapps.onboarding.presentation.buyer.amenities.AmenitiesAction
import com.zanoapps.onboarding.presentation.buyer.amenities.AmenityState
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationAction
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationBuyerState
import com.zanoapps.onboarding.presentation.buyer.propertyintent.PropertyIntentAction
import com.zanoapps.onboarding.presentation.buyer.propertyintent.PropertyIntentState
import com.zanoapps.onboarding.presentation.buyer.thankyoubuyer.ThankYouAction


class OnBoardingBuyerViewModel : ViewModel() {

    // Amenity State
    private val _amenityState = mutableStateOf(AmenityState())
    val amenityState: AmenityState get() = _amenityState.value



    fun onAmenitiesAction(action: AmenitiesAction) {
        when (action) {
            is AmenitiesAction.OnPreferenceSelected -> {
                toggleAmenitySelection(action.amenity)
            }

            else -> Unit
        }
    }

    private fun toggleAmenitySelection(amenity: Amenity) {
        val currentAmenities = _amenityState.value.savedAmenities.toMutableList()

        if (currentAmenities.contains(amenity)) {
            currentAmenities.remove(amenity)
        } else {
            currentAmenities.add(amenity)
        }

        _amenityState.value = _amenityState.value.copy(
            savedAmenities = currentAmenities
        )
    }


    fun clearAmenities() {
        _amenityState.value = _amenityState.value.copy(
            savedAmenities = emptyList()
        )
    }


    fun updateAmenitiesProgress(progress: Float) {
        _amenityState.value = _amenityState.value.copy(
            progress = progress
        )
    }


    private val _lifeSituationState = mutableStateOf(CurrentLifeSituationBuyerState())
    val lifeSituation: CurrentLifeSituationBuyerState get() = _lifeSituationState.value

    fun onLifeSituationAction(action: CurrentLifeSituationAction) {
        when (action) {
            is CurrentLifeSituationAction.OnPreferenceSelected -> {
                toggleLifeSituationSelection(action.preference)
            }

            else -> Unit
        }
    }

    private fun toggleLifeSituationSelection(lifeSituation: LifeSituation) {
        val currentSelection = _lifeSituationState.value.savedLifeSituation


        _lifeSituationState.value = _lifeSituationState.value.copy(
            savedLifeSituation = lifeSituation
        )
    }


    fun clearLifeSituationOptions() {
        _lifeSituationState.value = _lifeSituationState.value.copy(
            savedLifeSituation = null
        )
    }


    fun updateLifeSituationProgress(progress: Float) {
        _lifeSituationState.value = _lifeSituationState.value.copy(
            progress = progress
        )
    }


    // PropertyIntent State
    private val _propertyIntentState = mutableStateOf(PropertyIntentState())
    val propertyIntentState: PropertyIntentState get() = _propertyIntentState.value

    fun onPropertyIntentAction(action: PropertyIntentAction) {
        when (action) {
            is PropertyIntentAction.OnPreferenceSelected -> {
                togglePropertyIntentSelection(action.propertyIntent)
            }

            else -> Unit
        }
    }

    private fun togglePropertyIntentSelection(propertyIntent: PropertyIntent) {
        val currentSelection = _propertyIntentState.value.propertyIntent


        _propertyIntentState.value = _propertyIntentState.value.copy(
            propertyIntent = propertyIntent
        )
    }


    fun clearPropertyIntentOptions() {
        _propertyIntentState.value = _propertyIntentState.value.copy(
            propertyIntent = null
        )
    }


    fun updatePropertyIntentProgress(progress: Float) {
        _propertyIntentState.value = _propertyIntentState.value.copy(
            progress = progress
        )
    }

}



