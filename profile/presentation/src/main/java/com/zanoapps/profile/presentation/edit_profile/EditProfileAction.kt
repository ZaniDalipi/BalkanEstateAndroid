package com.zanoapps.profile.presentation.edit_profile

sealed interface EditProfileAction {
    data object OnBackClick : EditProfileAction
    data object OnSaveClick : EditProfileAction
    data object OnChangePhotoClick : EditProfileAction
    data object OnDeleteAccountClick : EditProfileAction
    data object OnDismissDeleteDialog : EditProfileAction
    data object OnConfirmDeleteAccount : EditProfileAction
}
