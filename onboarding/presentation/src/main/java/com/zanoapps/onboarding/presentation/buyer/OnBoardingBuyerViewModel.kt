package com.zanoapps.onboarding.presentation.buyer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zanoapps.onboarding.presentation.clientintent.ClientIntentState

class OnBoardingBuyerViewModel : ViewModel() {

    var state by mutableStateOf(BuyerPreferenceState())
        private set


}