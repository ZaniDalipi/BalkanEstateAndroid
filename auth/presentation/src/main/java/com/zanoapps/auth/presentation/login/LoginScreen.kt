package com.zanoapps.auth.presentation.login

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.EyeClosedIcon
import com.zanoapps.core.presentation.designsystem.EyeOpenedIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            LoginEvent.LoginSuccess -> onLoginSuccess()
            LoginEvent.NavigateToRegister -> onNavigateToRegister()
            LoginEvent.NavigateToForgotPassword -> Unit
            is LoginEvent.Error -> Unit
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Icon(
            imageVector = BalkanEstateLogo,
            contentDescription = "Balkan Estate",
            modifier = Modifier.size(80.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(Modifier.height(16.dp))
        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Text("Sign in to your account", fontSize = 14.sp, color = BalkanEstateGray)

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

        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
            label = { Text("Email Address") },
            leadingIcon = { Icon(EmailIcon, null, Modifier.size(20.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
            label = { Text("Password") },
            trailingIcon = {
                IconButton(onClick = { onAction(LoginAction.OnTogglePasswordVisibility) }) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = "Toggle password",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue),
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = { onAction(LoginAction.OnToggleRememberMe) },
                    colors = CheckboxDefaults.colors(checkedColor = BalkanEstatePrimaryBlue)
                )
                Text("Remember me", fontSize = 13.sp, color = BalkanEstateGray)
            }
            Text(
                "Forgot Password?",
                fontSize = 13.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onAction(LoginAction.OnForgotPasswordClick) }
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onAction(LoginAction.OnLoginClick) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("Sign In", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(Modifier.weight(1f), color = Color(0xFFE2E8F0))
            Text("  or continue with  ", fontSize = 12.sp, color = BalkanEstateGray)
            HorizontalDivider(Modifier.weight(1f), color = Color(0xFFE2E8F0))
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { onAction(LoginAction.OnGoogleLoginClick) },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Google") }
            OutlinedButton(
                onClick = { onAction(LoginAction.OnFacebookLoginClick) },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Facebook") }
        }

        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.Center) {
            Text("Don't have an account? ", color = BalkanEstateGray)
            Text(
                "Sign Up",
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onAction(LoginAction.OnRegisterClick) }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    BalkanEstateTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}
