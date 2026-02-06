package com.zanoapps.auth.presentation.login

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.EyeClosedIcon
import com.zanoapps.core.presentation.designsystem.EyeOpenedIcon
import com.zanoapps.core.presentation.designsystem.GoogleIcon
import com.zanoapps.core.presentation.designsystem.Poppins

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var passwordVisible by remember { mutableStateOf(false) }

    LoginScreen(
        email = email,
        password = password,
        isLoading = isLoading,
        selectedTabIndex = selectedTabIndex,
        passwordVisible = passwordVisible,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onTabSelected = { selectedTabIndex = it },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        onLoginClick = {
            isLoading = true
            // TODO: Implement actual login
            onLoginSuccess()
        },
        onGoogleSignInClick = {
            isLoading = true
            // TODO: Implement Google sign-in
            onLoginSuccess()
        },
        onForgotPasswordClick = {
            // TODO: Navigate to forgot password
        },
        onClose = onClose,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    email: String,
    password: String,
    isLoading: Boolean,
    selectedTabIndex: Int,
    passwordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTabSelected: (Int) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Log In", "Create Account")

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = CrossIcon,
                        contentDescription = "Close",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Icon(
                imageVector = BalkanEstateLogo,
                contentDescription = "Balkan Estate Logo",
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome text
            Text(
                text = "Welcome back! Please enter your details.",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFFF5F5F5),
                contentColor = BalkanEstatePrimaryBlue,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            height = 3.dp,
                            color = BalkanEstatePrimaryBlue
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) BalkanEstatePrimaryBlue else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                placeholder = { Text("Enter your email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = BalkanEstatePrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (passwordVisible) EyeOpenedIcon else EyeClosedIcon,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = BalkanEstatePrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot password
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot password?",
                    color = BalkanEstatePrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = onLoginClick,
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue,
                    disabledContainerColor = BalkanEstatePrimaryBlue.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = if (selectedTabIndex == 0) "Log In" else "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with "Or continue with"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray
                )
                Text(
                    text = "Or continue with",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google sign-in button
            OutlinedButton(
                onClick = onGoogleSignInClick,
                enabled = !isLoading,
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = GoogleIcon,
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    BalkanEstateTheme {
        LoginScreen(
            email = "",
            password = "",
            isLoading = false,
            selectedTabIndex = 0,
            passwordVisible = false,
            onEmailChange = {},
            onPasswordChange = {},
            onTabSelected = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
            onGoogleSignInClick = {},
            onForgotPasswordClick = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenFilledPreview() {
    BalkanEstateTheme {
        LoginScreen(
            email = "user@example.com",
            password = "password123",
            isLoading = false,
            selectedTabIndex = 0,
            passwordVisible = false,
            onEmailChange = {},
            onPasswordChange = {},
            onTabSelected = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
            onGoogleSignInClick = {},
            onForgotPasswordClick = {},
            onClose = {}
        )
    }
}
