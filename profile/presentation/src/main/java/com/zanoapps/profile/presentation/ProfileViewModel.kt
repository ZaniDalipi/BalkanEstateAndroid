package com.zanoapps.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.LoadProfile -> loadProfile()
            ProfileAction.OnEditProfile -> {
                state = state.copy(isEditProfileDialogVisible = true)
            }
            ProfileAction.OnDismissEditProfile -> {
                state = state.copy(isEditProfileDialogVisible = false)
            }
            is ProfileAction.OnSaveProfile -> {
                saveProfile(action.name, action.phone)
            }
            ProfileAction.OnChangePassword -> {
                // Navigate to change password
            }
            ProfileAction.OnToggleNotifications -> {
                state = state.copy(notificationsEnabled = !state.notificationsEnabled)
            }
            ProfileAction.OnToggleDarkMode -> {
                state = state.copy(darkModeEnabled = !state.darkModeEnabled)
            }
            ProfileAction.OnMyListings -> {
                viewModelScope.launch {
                    eventChannel.send(ProfileEvent.NavigateToMyListings)
                }
            }
            ProfileAction.OnMyFavourites -> {
                viewModelScope.launch {
                    eventChannel.send(ProfileEvent.NavigateToFavourites)
                }
            }
            ProfileAction.OnMySavedSearches -> {
                viewModelScope.launch {
                    eventChannel.send(ProfileEvent.NavigateToSavedSearches)
                }
            }
            ProfileAction.OnHelpCenter -> {
                // Open help center
            }
            ProfileAction.OnPrivacyPolicy -> {
                // Open privacy policy
            }
            ProfileAction.OnTermsOfService -> {
                // Open terms of service
            }
            ProfileAction.OnAboutUs -> {
                // Open about us
            }
            ProfileAction.OnLogoutClick -> {
                state = state.copy(isLogoutDialogVisible = true)
            }
            ProfileAction.OnConfirmLogout -> {
                logout()
            }
            ProfileAction.OnDismissLogout -> {
                state = state.copy(isLogoutDialogVisible = false)
            }
            ProfileAction.OnDeleteAccount -> {
                // Show delete account confirmation
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val user = getMockUser()
            state = state.copy(
                user = user,
                isLoading = false
            )
        }
    }

    private fun saveProfile(name: String, phone: String) {
        viewModelScope.launch {
            state.user?.let { currentUser ->
                val updatedUser = currentUser.copy(name = name, phone = phone)
                state = state.copy(
                    user = updatedUser,
                    isEditProfileDialogVisible = false
                )
                eventChannel.send(ProfileEvent.ProfileUpdated)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            state = state.copy(
                isLoggedIn = false,
                isLogoutDialogVisible = false
            )
            eventChannel.send(ProfileEvent.LoggedOut)
        }
    }

    private fun getMockUser(): UserProfile {
        return UserProfile(
            id = "user1",
            name = "Besmir Kola",
            email = "besmir.kola@example.com",
            phone = "+355 69 123 4567",
            avatarUrl = null,
            memberSince = "January 2024",
            listingsCount = 5,
            favouritesCount = 12,
            savedSearchesCount = 3
        )
    }
}
