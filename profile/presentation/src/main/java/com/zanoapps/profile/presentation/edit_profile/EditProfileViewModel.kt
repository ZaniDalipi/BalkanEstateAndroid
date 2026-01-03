package com.zanoapps.profile.presentation.edit_profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.repository.ProfileRepository
import com.zanoapps.profile.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(EditProfileState())
        private set

    private val eventChannel = Channel<EditProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadProfile()
        observeFields()
    }

    private fun observeFields() {
        combine(
            snapshotFlow { state.firstName.text },
            snapshotFlow { state.lastName.text }
        ) { firstName, lastName ->
            val isFirstNameValid = firstName.length >= 2
            val isLastNameValid = lastName.length >= 2

            state = state.copy(
                isFirstNameValid = isFirstNameValid,
                isLastNameValid = isLastNameValid,
                canSave = isFirstNameValid && isLastNameValid
            )
        }.launchIn(viewModelScope)
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = profileRepository.getProfile()) {
                is Result.Success -> {
                    val profile = result.data
                    state = state.copy(
                        firstName = TextFieldState(initialText = profile.firstName),
                        lastName = TextFieldState(initialText = profile.lastName),
                        email = profile.email,
                        phoneNumber = TextFieldState(initialText = profile.phoneNumber ?: ""),
                        bio = TextFieldState(initialText = profile.bio ?: ""),
                        profileImageUrl = profile.profileImageUrl,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(isLoading = false)
                    eventChannel.send(
                        EditProfileEvent.Error(UiText.StringResource(R.string.error_loading_profile))
                    )
                }
            }
        }
    }

    fun onAction(action: EditProfileAction) {
        when (action) {
            EditProfileAction.OnBackClick -> Unit // Handled by navigation
            EditProfileAction.OnSaveClick -> saveProfile()
            EditProfileAction.OnChangePhotoClick -> {
                // TODO: Open image picker
            }
            EditProfileAction.OnDeleteAccountClick -> {
                state = state.copy(showDeleteDialog = true)
            }
            EditProfileAction.OnDismissDeleteDialog -> {
                state = state.copy(showDeleteDialog = false)
            }
            EditProfileAction.OnConfirmDeleteAccount -> deleteAccount()
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            state = state.copy(isSaving = true)
            val result = profileRepository.updateProfile(
                firstName = state.firstName.text.toString().trim(),
                lastName = state.lastName.text.toString().trim(),
                phoneNumber = state.phoneNumber.text.toString().trim().ifEmpty { null },
                bio = state.bio.text.toString().trim().ifEmpty { null }
            )
            state = state.copy(isSaving = false)

            when (result) {
                is Result.Success -> {
                    eventChannel.send(EditProfileEvent.ProfileUpdated)
                }
                is Result.Error -> {
                    eventChannel.send(
                        EditProfileEvent.Error(UiText.StringResource(R.string.error_updating_profile))
                    )
                }
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            state = state.copy(showDeleteDialog = false, isSaving = true)
            when (val result = profileRepository.deleteAccount()) {
                is Result.Success -> {
                    eventChannel.send(EditProfileEvent.AccountDeleted)
                }
                is Result.Error -> {
                    state = state.copy(isSaving = false)
                    eventChannel.send(
                        EditProfileEvent.Error(UiText.StringResource(R.string.error_deleting_account))
                    )
                }
            }
        }
    }
}
