package com.zanoapps.profile.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private fun loadProfile() {
        profileRepository.getProfile()
            .onEach { profile ->
                state = state.copy(userProfile = profile)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnLoadProfile -> { /* Already loaded with default values */ }
            ProfileAction.OnEditProfileClick -> {
                state = state.copy(
                    isEditing = true,
                    editFirstName = state.userProfile.firstName,
                    editLastName = state.userProfile.lastName,
                    editEmail = state.userProfile.email,
                    editPhone = state.userProfile.phone,
                    editBio = state.userProfile.bio,
                    editLocation = state.userProfile.location
                )
            }
            ProfileAction.OnCancelEdit -> {
                state = state.copy(isEditing = false)
            }
            ProfileAction.OnSaveProfile -> saveProfile()
            is ProfileAction.OnFirstNameChanged -> state = state.copy(editFirstName = action.firstName)
            is ProfileAction.OnLastNameChanged -> state = state.copy(editLastName = action.lastName)
            is ProfileAction.OnEmailChanged -> state = state.copy(editEmail = action.email)
            is ProfileAction.OnPhoneChanged -> state = state.copy(editPhone = action.phone)
            is ProfileAction.OnBioChanged -> state = state.copy(editBio = action.bio)
            is ProfileAction.OnLocationChanged -> state = state.copy(editLocation = action.location)
            ProfileAction.OnMyListingsClick -> {
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToMyListings) }
            }
            ProfileAction.OnSavedPropertiesClick -> {
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToSavedProperties) }
            }
            ProfileAction.OnSavedSearchesClick -> {
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToSavedSearches) }
            }
            ProfileAction.OnNotificationSettingsClick -> {
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToNotificationSettings) }
            }
            ProfileAction.OnSubscriptionClick -> {
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToSubscription) }
            }
            ProfileAction.OnHelpClick -> {}
            ProfileAction.OnPrivacyPolicyClick -> {}
            ProfileAction.OnTermsClick -> {}
            ProfileAction.OnLogoutClick -> state = state.copy(showLogoutDialog = true)
            ProfileAction.OnConfirmLogout -> {
                state = state.copy(showLogoutDialog = false)
                viewModelScope.launch { eventChannel.send(ProfileEvent.NavigateToLogin) }
            }
            ProfileAction.OnDismissLogout -> state = state.copy(showLogoutDialog = false)
            ProfileAction.OnDeleteAccountClick -> state = state.copy(showDeleteAccountDialog = true)
            ProfileAction.OnConfirmDeleteAccount -> {
                state = state.copy(showDeleteAccountDialog = false)
                viewModelScope.launch {
                    profileRepository.deleteAccount()
                    eventChannel.send(ProfileEvent.NavigateToLogin)
                }
            }
            ProfileAction.OnDismissDeleteAccount -> state = state.copy(showDeleteAccountDialog = false)
            ProfileAction.OnChangePasswordClick -> {}
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            state = state.copy(isSaving = true)
            val updatedProfile = state.userProfile.copy(
                firstName = state.editFirstName,
                lastName = state.editLastName,
                email = state.editEmail,
                phone = state.editPhone,
                bio = state.editBio,
                location = state.editLocation
            )
            profileRepository.updateProfile(updatedProfile)
            state = state.copy(
                userProfile = updatedProfile,
                isEditing = false,
                isSaving = false
            )
            eventChannel.send(ProfileEvent.ProfileSaved)
        }
    }
}
