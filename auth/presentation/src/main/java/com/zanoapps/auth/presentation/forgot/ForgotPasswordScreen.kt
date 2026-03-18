package com.zanoapps.auth.presentation.forgot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreenRoot(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            ForgotPasswordEvent.NavigateBack -> onNavigateBack()
            ForgotPasswordEvent.NavigateToLogin -> onNavigateToLogin()
            is ForgotPasswordEvent.Error -> Unit
        }
    }

    ForgotPasswordScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { onAction(ForgotPasswordAction.OnBackClick) }) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        if (state.isEmailSent) {
            // Success state
            SuccessContent(onAction = onAction)
        } else {
            // Form state
            FormContent(state = state, onAction = onAction)
        }
    }
}

@Composable
private fun FormContent(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit
) {
    // Email icon
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(
                BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = EmailIcon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(36.dp)
        )
    }

    Spacer(Modifier.height(24.dp))

    Text(
        text = "Forgot Password?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Enter your email address and we'll send you a link to reset your password",
        fontSize = 14.sp,
        color = BalkanEstateGray,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(Modifier.height(32.dp))

    // Error message
    if (state.errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BalkanEstateRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(state.errorMessage, color = BalkanEstateRed, fontSize = 14.sp)
        }
        Spacer(Modifier.height(16.dp))
    }

    // Email field
    OutlinedTextField(
        value = state.email,
        onValueChange = { onAction(ForgotPasswordAction.OnEmailChanged(it)) },
        label = { Text("Email Address") },
        leadingIcon = {
            Icon(
                imageVector = EmailIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BalkanEstatePrimaryBlue
        ),
        singleLine = true,
        enabled = !state.isLoading
    )

    Spacer(Modifier.height(24.dp))

    // Send Reset Link button
    BalkanEstateActionButton(
        text = "Send Reset Link",
        isLoading = state.isLoading,
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.isLoading,
        onClick = { onAction(ForgotPasswordAction.OnSendResetLinkClick) }
    )

    Spacer(Modifier.height(24.dp))

    // Back to Login link
    Text(
        text = "Back to Login",
        fontSize = 14.sp,
        color = BalkanEstatePrimaryBlue,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.clickable { onAction(ForgotPasswordAction.OnBackToLoginClick) }
    )
}

@Composable
private fun SuccessContent(
    onAction: (ForgotPasswordAction) -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Checkmark icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        BalkanEstateGreen.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = "Success",
                    tint = BalkanEstateGreen,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Check Your Email",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "We've sent a password reset link to your email address. Please check your inbox and follow the instructions.",
                fontSize = 14.sp,
                color = BalkanEstateGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(32.dp))

            BalkanEstateActionButton(
                text = "Back to Login",
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(ForgotPasswordAction.OnBackToLoginClick) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    BalkanEstateTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenSuccessPreview() {
    BalkanEstateTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(isEmailSent = true),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenErrorPreview() {
    BalkanEstateTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                email = "invalid",
                errorMessage = "Please enter a valid email address"
            ),
            onAction = {}
        )
    }
}
