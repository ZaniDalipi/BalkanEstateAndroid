package com.zanoapps.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.auth.presentation.R
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.EyeClosedIcon
import com.zanoapps.core.presentation.designsystem.EyeOpenedIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.Error -> {
                    keyboardController?.hide()
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                LoginEvent.LoginSuccess -> {
                    keyboardController?.hide()
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> onNavigateToRegister()
                else -> viewModel.onAction(action)
            }
        }
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
        Spacer(modifier = Modifier.height(48.dp))

        // Logo
        Icon(
            imageVector = BalkanEstateLogo,
            contentDescription = "BalkanEstate Logo",
            modifier = Modifier.size(80.dp),
            tint = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = stringResource(R.string.login_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Email Field
        AuthTextField(
            state = state.email,
            label = stringResource(R.string.email_label),
            hint = stringResource(R.string.email_hint),
            keyboardType = KeyboardType.Email,
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
            onToggleVisibility = { onAction(LoginAction.OnTogglePasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onAction(LoginAction.OnForgotPasswordClick) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        BalkanEstateActionButton(
            text = stringResource(R.string.login_button),
            isLoading = state.isLoggingIn,
            enabled = state.canLogin && !state.isLoggingIn,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(LoginAction.OnLoginClick) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Divider with "Or continue with"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Text(
                text = stringResource(R.string.or_continue_with),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social Login Buttons
        BalkanEstateOutlinedActionButton(
            text = stringResource(R.string.sign_in_with_google),
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(LoginAction.OnGoogleSignInClick) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        BalkanEstateOutlinedActionButton(
            text = stringResource(R.string.sign_in_with_facebook),
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(LoginAction.OnFacebookSignInClick) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Register Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_account),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.register_link),
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onAction(LoginAction.OnRegisterClick) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AuthTextField(
    state: TextFieldState,
    label: String,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
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
            lineLimits = TextFieldLineLimits.SingleLine,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (state.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerBox()
                    }
                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(12.dp))
                        trailingIcon()
                    }
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

@Composable
fun PasswordTextField(
    state: TextFieldState,
    label: String,
    hint: String,
    isPasswordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(BalkanEstatePrimaryBlue),
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .onFocusChanged { isFocused = it.isFocused },
            decorator = { innerBox ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (state.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerBox()
                    }
                    IconButton(
                        onClick = onToggleVisibility,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = Color.Gray
                        )
                    }
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
private fun LoginScreenPreview() {
    BalkanEstateTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}
