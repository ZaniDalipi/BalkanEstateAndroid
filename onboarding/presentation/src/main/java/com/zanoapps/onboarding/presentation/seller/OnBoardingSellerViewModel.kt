package com.zanoapps.onboarding.presentation.seller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zanoapps.onboarding.domain.enums.seller.MainGoal
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.domain.enums.seller.SellingTime
import com.zanoapps.onboarding.presentation.seller.maingoal.SellerMainGoalAction
import com.zanoapps.onboarding.presentation.seller.maingoal.SellerMainGoalState
import com.zanoapps.onboarding.presentation.seller.propertytype.PropertyTypeState
import com.zanoapps.onboarding.presentation.seller.propertytype.SellerPropertyTypeAction
import com.zanoapps.onboarding.presentation.seller.sellercompletion.SellerCompletionState
import com.zanoapps.onboarding.presentation.seller.sellingtime.SellingTimeAction
import com.zanoapps.onboarding.presentation.seller.sellingtime.SellingTimeState

class OnBoardingSellerViewModel: ViewModel() {


    private val _propertyTypeStateState = mutableStateOf(PropertyTypeState())
    val propertyTypeState get() = _propertyTypeStateState.value

    private val _sellingTimeState = mutableStateOf(SellingTimeState())
    val sellingTimeState get() = _sellingTimeState.value

    private val _mainGoalState = mutableStateOf(SellerMainGoalState())
    val mainGoalState get() = _mainGoalState.value

    private val _sellerCompletionState = mutableStateOf(SellerCompletionState())
    val sellerCompletionState get() = _sellerCompletionState.value


    fun updatePropertyIntentProgress(progress: Float) {
        _propertyTypeStateState.value = _propertyTypeStateState.value.copy(
            progress = progress
        )
    }

    fun updateSellingTimeProgress(progress: Float) {
        _sellingTimeState.value = _sellingTimeState.value.copy(
            progress = progress
        )
    }

    fun updateMainGoalProgress(progress: Float) {
        _mainGoalState.value = _mainGoalState.value.copy(
            progress = progress
        )
    }

    init {
        updatePropertyIntentProgress( propertyTypeState.progress)
        updateSellingTimeProgress(sellingTimeState.progress)
        updateMainGoalProgress(mainGoalState.progress)
    }

    fun onPropertyTypeAction(action: SellerPropertyTypeAction) {
        when (action) {
            is SellerPropertyTypeAction.OnPreferenceSelected -> {
                togglePropertyTypeSelection(action.preference)
            }

            else -> Unit
        }
    }

    private fun togglePropertyTypeSelection(sellerPropertyType: PropertyTypeSeller) {


        _propertyTypeStateState.value = _propertyTypeStateState.value.copy(
            propertyTypeSeller = sellerPropertyType
        )
    }


 /*   fun clearLifeSituationOptions() {
        _propertyTypeStateState.value = _propertyTypeStateState.value.copy(
            propertyTypeSeller = null
        )
    }


    fun updateLifeSituationProgress(progress: Float) {
        _propertyTypeStateState.value = _propertyTypeStateState.value.copy(
            progress = progress
        )
    }*/


    fun onMainGoalAction(mainGoalAction: SellerMainGoalAction) {
        when(mainGoalAction) {
            is SellerMainGoalAction.OnPreferenceSelected -> {
                toggleMainGoalSelection(mainGoalAction.preference)
            }
            else -> Unit
        }

    }

    private fun toggleMainGoalSelection(mainGoal: MainGoal) {
        _mainGoalState.value = _mainGoalState.value.copy(
            sellerMainGoal = mainGoal
        )
    }


    fun onSellingTimeAction(sellingTimeAction: SellingTimeAction) {
        when(sellingTimeAction) {
            is SellingTimeAction.OnPreferenceSelected -> {
                toggleSellingTimeSelection(sellingTimeAction.preference)
            }
            else -> Unit
        }

    }

    private fun toggleSellingTimeSelection(sellingTime: SellingTime) {
        _sellingTimeState.value = _sellingTimeState.value.copy(
            sellingTime = sellingTime
        )
    }






}