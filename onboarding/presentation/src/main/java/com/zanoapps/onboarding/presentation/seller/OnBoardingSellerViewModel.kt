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

class OnBoardingSellerViewModel : ViewModel() {

    private val _propertyTypeState = mutableStateOf(PropertyTypeState())
    val propertyTypeState get() = _propertyTypeState.value

    private val _sellingTimeState = mutableStateOf(SellingTimeState())
    val sellingTimeState get() = _sellingTimeState.value

    private val _mainGoalState = mutableStateOf(SellerMainGoalState())
    val mainGoalState get() = _mainGoalState.value

    private val _sellerCompletionState = mutableStateOf(SellerCompletionState())
    val sellerCompletionState get() = _sellerCompletionState.value

    init {
        _propertyTypeState.value = _propertyTypeState.value.copy(progress = propertyTypeState.progress)
        _sellingTimeState.value = _sellingTimeState.value.copy(progress = sellingTimeState.progress)
        _mainGoalState.value = _mainGoalState.value.copy(progress = mainGoalState.progress)
    }

    fun onPropertyTypeAction(action: SellerPropertyTypeAction) {
        when (action) {
            is SellerPropertyTypeAction.OnPreferenceSelected -> {
                _propertyTypeState.value = _propertyTypeState.value.copy(
                    propertyTypeSeller = action.preference
                )
            }
            is SellerPropertyTypeAction.OnProgressUpdate -> {
                _propertyTypeState.value = _propertyTypeState.value.copy(
                    progress = action.progress
                )
            }
        }
    }

    fun onMainGoalAction(mainGoalAction: SellerMainGoalAction) {
        when (mainGoalAction) {
            is SellerMainGoalAction.OnPreferenceSelected -> {
                _mainGoalState.value = _mainGoalState.value.copy(
                    sellerMainGoal = mainGoalAction.preference
                )
            }
            is SellerMainGoalAction.OnProgressUpdate -> {
                _mainGoalState.value = _mainGoalState.value.copy(
                    progress = mainGoalAction.progress
                )
            }
        }
    }

    fun onSellingTimeAction(sellingTimeAction: SellingTimeAction) {
        when (sellingTimeAction) {
            is SellingTimeAction.OnPreferenceSelected -> {
                _sellingTimeState.value = _sellingTimeState.value.copy(
                    sellingTime = sellingTimeAction.preference
                )
            }
            is SellingTimeAction.OnProgressUpdate -> {
                _sellingTimeState.value = _sellingTimeState.value.copy(
                    progress = sellingTimeAction.progress
                )
            }
        }
    }
}
