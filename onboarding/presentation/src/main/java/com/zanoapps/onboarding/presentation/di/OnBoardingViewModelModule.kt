package com.zanoapps.onboarding.presentation.di

import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import com.zanoapps.onboarding.presentation.clientintent.ClientIntentViewModel
import com.zanoapps.onboarding.presentation.seller.OnBoardingSellerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onBoardingViewModelModule = module {
    viewModelOf(::ClientIntentViewModel)
    viewModelOf(::OnBoardingBuyerViewModel)
    viewModelOf(::OnBoardingSellerViewModel)
}