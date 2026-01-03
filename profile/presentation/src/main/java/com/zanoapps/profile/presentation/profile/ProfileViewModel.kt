package com.zanoapps.profile.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.repository.ProfileRepository
import com.zanoapps.profile.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnBackClick -> Unit // Handled by navigation
            ProfileAction.OnEditProfileClick -> Unit // Handled by navigation
            ProfileAction.OnSavedSearchesClick -> Unit // Handled by navigation
            ProfileAction.OnFavouritesClick -> Unit // Handled by navigation
            ProfileAction.OnMyListingsClick -> Unit // Handled by navigation
            ProfileAction.OnSettingsClick -> Unit // Handled by navigation
            ProfileAction.OnHelpClick -> Unit // Handled by navigation
            ProfileAction.OnLogoutClick -> logout()
            ProfileAction.OnRefresh -> refresh()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = profileRepository.getProfile()) {
                is Result.Success -> {
                    state = state.copy(
                        profile = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(isLoading = false)
                    eventChannel.send(
                        ProfileEvent.Error(UiText.StringResource(R.string.error_loading_profile))
                    )
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            when (val result = profileRepository.getProfile()) {
                is Result.Success -> {
                    state = state.copy(
                        profile = result.data,
                        isRefreshing = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(isRefreshing = false)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            // Clear session and navigate to login
            eventChannel.send(ProfileEvent.LogoutSuccess)
        }
    }
}
