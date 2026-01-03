package com.zanoapps.onboarding.data.di

import com.zanoapps.onboarding.data.repository.OnboardingRepositoryImpl
import com.zanoapps.onboarding.domain.repository.OnboardingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val onboardingDataModule = module {
    singleOf(::OnboardingRepositoryImpl).bind<OnboardingRepository>()
}
