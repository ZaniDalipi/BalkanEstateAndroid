package com.zanoapps.profile.presentation.edit_profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CameraIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.profile.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreenRoot(
    viewModel: EditProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onProfileUpdated: () -> Unit,
    onAccountDeleted: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is EditProfileEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                EditProfileEvent.ProfileUpdated -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.profile_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                    onProfileUpdated()
                }
                EditProfileEvent.AccountDeleted -> {
                    onAccountDeleted()
                }
            }
        }
    }

    EditProfileScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                EditProfileAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    state: EditProfileState,
    onAction: (EditProfileAction) -> Unit
) {
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onAction(EditProfileAction.OnDismissDeleteDialog) },
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = { Text(stringResource(R.string.delete_account_message)) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(EditProfileAction.OnConfirmDeleteAccount) }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(EditProfileAction.OnDismissDeleteDialog) }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(EditProfileAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.profileImageUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.profileImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${state.firstName.text.firstOrNull()?.uppercaseChar() ?: ""}${state.lastName.text.firstOrNull()?.uppercaseChar() ?: ""}",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = BalkanEstatePrimaryBlue
                            )
                        }
                    }

                    // Camera Button
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue)
                            .clickable { onAction(EditProfileAction.OnChangePhotoClick) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = CameraIcon,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // First Name
                ProfileTextField(
                    state = state.firstName,
                    label = stringResource(R.string.first_name_label),
                    placeholder = stringResource(R.string.first_name_hint),
                    error = if (!state.isFirstNameValid) stringResource(R.string.error_invalid_name) else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Last Name
                ProfileTextField(
                    state = state.lastName,
                    label = stringResource(R.string.last_name_label),
                    placeholder = stringResource(R.string.last_name_hint),
                    error = if (!state.isLastNameValid) stringResource(R.string.error_invalid_name) else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email (read-only)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.email_label),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE5E7EB))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = state.email,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number
                ProfileTextField(
                    state = state.phoneNumber,
                    label = stringResource(R.string.phone_label),
                    placeholder = stringResource(R.string.phone_hint),
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bio
                ProfileTextField(
                    state = state.bio,
                    label = stringResource(R.string.bio_label),
                    placeholder = stringResource(R.string.bio_hint),
                    singleLine = false,
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                BalkanEstateActionButton(
                    text = stringResource(R.string.save_changes),
                    isLoading = state.isSaving,
                    enabled = state.canSave && !state.isSaving,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAction(EditProfileAction.OnSaveClick) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Delete Account
                Text(
                    text = stringResource(R.string.delete_account),
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onAction(EditProfileAction.OnDeleteAccountClick) }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    state: TextFieldState,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    error: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            lineLimits = if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.MultiLine(
                minHeightInLines = minLines,
                maxHeightInLines = 5
            ),
            cursorBrush = SolidColor(BalkanEstatePrimaryBlue),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF8F9FA))
                .border(
                    width = 1.dp,
                    color = when {
                        error != null -> Color.Red
                        isFocused -> BalkanEstatePrimaryBlue
                        else -> Color.Transparent
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .onFocusChanged { isFocused = it.isFocused },
            decorator = { innerBox ->
                Box {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    innerBox()
                }
            }
        )

        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    BalkanEstateTheme {
        EditProfileScreen(
            state = EditProfileState(
                email = "john.doe@example.com"
            ),
            onAction = {}
        )
    }
}
