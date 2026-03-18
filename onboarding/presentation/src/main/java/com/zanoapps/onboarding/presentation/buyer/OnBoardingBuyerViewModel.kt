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


class OnBoardingBuyerViewModel : ViewModel() {

    // Amenity State
    private val _amenityState = mutableStateOf(AmenityState())
    val amenityState: AmenityState get() = _amenityState.value

    // Life Situation State
    private val _lifeSituationState = mutableStateOf(CurrentLifeSituationBuyerState())
    val lifeSituation: CurrentLifeSituationBuyerState get() = _lifeSituationState.value

    // Property Intent State
    private val _propertyIntentState = mutableStateOf(PropertyIntentState())
    val propertyIntentState: PropertyIntentState get() = _propertyIntentState.value

    fun onAmenitiesAction(action: AmenitiesAction) {
        when (action) {
            is AmenitiesAction.OnPreferenceSelected -> {
                val currentAmenities = _amenityState.value.savedAmenities.toMutableList()
                if (currentAmenities.contains(action.amenity)) {
                    currentAmenities.remove(action.amenity)
                } else {
                    currentAmenities.add(action.amenity)
                }
                _amenityState.value = _amenityState.value.copy(savedAmenities = currentAmenities)
            }
            AmenitiesAction.OnBackClick -> Unit
            AmenitiesAction.OnNextClick -> Unit
            AmenitiesAction.OnSkipClick -> Unit
        }
    }

    fun updateAmenitiesProgress(progress: Float) {
        _amenityState.value = _amenityState.value.copy(progress = progress)
    }

    fun onLifeSituationAction(action: CurrentLifeSituationAction) {
        when (action) {
            is CurrentLifeSituationAction.OnPreferenceSelected -> {
                _lifeSituationState.value = _lifeSituationState.value.copy(
                    savedLifeSituation = action.preference
                )
            }
            CurrentLifeSituationAction.OnBackClick -> Unit
            CurrentLifeSituationAction.OnNextClick -> Unit
            CurrentLifeSituationAction.OnSkipClick -> Unit
        }
    }

    fun updateLifeSituationProgress(progress: Float) {
        _lifeSituationState.value = _lifeSituationState.value.copy(progress = progress)
    }

    fun onPropertyIntentAction(action: PropertyIntentAction) {
        when (action) {
            is PropertyIntentAction.OnPreferenceSelected -> {
                _propertyIntentState.value = _propertyIntentState.value.copy(
                    propertyIntent = action.propertyIntent
                )
            }
            PropertyIntentAction.OnBackClick -> Unit
            PropertyIntentAction.OnNextClick -> Unit
            PropertyIntentAction.OnSkipClick -> Unit
        }
    }

    fun updatePropertyIntentProgress(progress: Float) {
        _propertyIntentState.value = _propertyIntentState.value.copy(progress = progress)
    }
}
