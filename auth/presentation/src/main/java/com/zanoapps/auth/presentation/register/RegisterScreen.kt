package com.zanoapps.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.auth.presentation.R
import com.zanoapps.auth.presentation.login.AuthTextField
import com.zanoapps.auth.presentation.login.PasswordTextField
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.Error -> {
                    keyboardController?.hide()
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                RegisterEvent.RegistrationSuccess -> {
                    keyboardController?.hide()
                    Toast.makeText(
                        context,
                        context.getString(R.string.registration_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onRegistrationSuccess()
                }
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> onNavigateToLogin()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo
        Icon(
            imageVector = BalkanEstateLogo,
            contentDescription = "BalkanEstate Logo",
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = stringResource(R.string.register_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = stringResource(R.string.register_subtitle),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Fields Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthTextField(
                state = state.firstName,
                label = stringResource(R.string.first_name_label),
                hint = stringResource(R.string.first_name_hint),
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(
                        imageVector = PersonIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            AuthTextField(
                state = state.lastName,
                label = stringResource(R.string.last_name_label),
                hint = stringResource(R.string.last_name_hint),
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(
                        imageVector = PersonIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        AuthTextField(
            state = state.email,
            label = stringResource(R.string.email_label),
            hint = stringResource(R.string.email_hint),
            leadingIcon = {
                Icon(
                    imageVector = EmailIcon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        PasswordTextField(
            state = state.password,
            label = stringResource(R.string.password_label),
            hint = stringResource(R.string.password_hint),
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibility = { onAction(RegisterAction.OnTogglePasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Requirements
        PasswordRequirements(
            hasMinLength = state.passwordValidationState.hasMinLength,
            hasUppercase = state.passwordValidationState.hasUppercase,
            hasLowercase = state.passwordValidationState.hasLowercase,
            hasDigit = state.passwordValidationState.hasDigit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        PasswordTextField(
            state = state.confirmPassword,
            label = stringResource(R.string.confirm_password_label),
            hint = stringResource(R.string.confirm_password_hint),
            isPasswordVisible = state.isConfirmPasswordVisible,
            onToggleVisibility = { onAction(RegisterAction.OnToggleConfirmPasswordVisibility) },
            error = if (state.confirmPassword.text.isNotEmpty() && !state.doPasswordsMatch) {
                stringResource(R.string.error_passwords_not_match)
            } else null
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Terms Agreement
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.terms_agreement))
                append(" ")
                withStyle(style = SpanStyle(color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Medium)) {
                    append(stringResource(R.string.terms_of_service))
                }
                append(" ")
                append(stringResource(R.string.and))
                append(" ")
                withStyle(style = SpanStyle(color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Medium)) {
                    append(stringResource(R.string.privacy_policy))
                }
            },
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Register Button
        BalkanEstateActionButton(
            text = stringResource(R.string.register_button),
            isLoading = state.isRegistering,
            enabled = state.canRegister && !state.isRegistering,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(RegisterAction.OnRegisterClick) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.login_link),
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onAction(RegisterAction.OnLoginClick) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PasswordRequirements(
    hasMinLength: Boolean,
    hasUppercase: Boolean,
    hasLowercase: Boolean,
    hasDigit: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.password_requirements),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        PasswordRequirementItem(
            text = stringResource(R.string.password_min_length),
            isMet = hasMinLength
        )
        PasswordRequirementItem(
            text = stringResource(R.string.password_uppercase),
            isMet = hasUppercase
        )
        PasswordRequirementItem(
            text = stringResource(R.string.password_lowercase),
            isMet = hasLowercase
        )
        PasswordRequirementItem(
            text = stringResource(R.string.password_digit),
            isMet = hasDigit
        )
    }
}

@Composable
private fun PasswordRequirementItem(
    text: String,
    isMet: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Icon(
            imageVector = if (isMet) CheckIcon else CrossIcon,
            contentDescription = null,
            tint = if (isMet) BalkanEstateGreen else BalkanEstateRed,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isMet) BalkanEstateGreen else Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    BalkanEstateTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
