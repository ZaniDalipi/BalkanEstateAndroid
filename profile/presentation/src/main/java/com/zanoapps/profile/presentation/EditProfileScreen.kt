package com.zanoapps.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CameraIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins

data class EditProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val bio: String = "",
    val avatarUrl: String? = null
)

@Composable
fun EditProfileScreenRoot(
    state: EditProfileState,
    onBackClick: () -> Unit,
    onSaveClick: (EditProfileState) -> Unit,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EditProfileScreen(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        onChangePhotoClick = onChangePhotoClick,
        modifier = modifier
    )
}

@Composable
fun EditProfileScreen(
    state: EditProfileState,
    onBackClick: () -> Unit,
    onSaveClick: (EditProfileState) -> Unit,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editState by remember { mutableStateOf(state) }
    var hasChanges by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.DarkGray
                        )
                    }

                    Text(
                        text = "Edit Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    if (hasChanges) {
                        IconButton(
                            onClick = { onSaveClick(editState) },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save",
                                tint = BalkanEstateGreen
                            )
                        }
                    }
                }
            }

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Photo Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = PersonIcon,
                                    contentDescription = null,
                                    tint = BalkanEstatePrimaryBlue,
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BalkanEstatePrimaryBlue)
                                    .clickable { onChangePhotoClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = CameraIcon,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Change Profile Photo",
                            fontSize = 14.sp,
                            color = BalkanEstatePrimaryBlue,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onChangePhotoClick() }
                        )
                    }
                }

                // Personal Information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        ProfileTextField(
                            value = editState.firstName,
                            onValueChange = {
                                editState = editState.copy(firstName = it)
                                hasChanges = true
                            },
                            label = "First Name",
                            placeholder = "Enter your first name"
                        )

                        ProfileTextField(
                            value = editState.lastName,
                            onValueChange = {
                                editState = editState.copy(lastName = it)
                                hasChanges = true
                            },
                            label = "Last Name",
                            placeholder = "Enter your last name"
                        )

                        ProfileTextField(
                            value = editState.bio,
                            onValueChange = {
                                editState = editState.copy(bio = it)
                                hasChanges = true
                            },
                            label = "Bio",
                            placeholder = "Tell us about yourself",
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                }

                // Contact Information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Contact Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        ProfileTextField(
                            value = editState.email,
                            onValueChange = {
                                editState = editState.copy(email = it)
                                hasChanges = true
                            },
                            label = "Email",
                            placeholder = "Enter your email",
                            keyboardType = KeyboardType.Email
                        )

                        ProfileTextField(
                            value = editState.phone,
                            onValueChange = {
                                editState = editState.copy(phone = it)
                                hasChanges = true
                            },
                            label = "Phone",
                            placeholder = "Enter your phone number",
                            keyboardType = KeyboardType.Phone
                        )

                        ProfileTextField(
                            value = editState.location,
                            onValueChange = {
                                editState = editState.copy(location = it)
                                hasChanges = true
                            },
                            label = "Location",
                            placeholder = "Enter your location"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Save Button
                Button(
                    onClick = { onSaveClick(editState) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue,
                        disabledContainerColor = Color.LightGray
                    ),
                    enabled = hasChanges
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            minLines = minLines,
            maxLines = maxLines
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    BalkanEstateTheme {
        EditProfileScreen(
            state = EditProfileState(
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                phone = "+355 69 123 4567",
                location = "Tirana, Albania"
            ),
            onBackClick = {},
            onSaveClick = {},
            onChangePhotoClick = {}
        )
    }
}
