package com.zanoapps.auth.presentation.register

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.EyeClosedIcon
import com.zanoapps.core.presentation.designsystem.EyeOpenedIcon
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            RegisterEvent.RegisterSuccess -> onRegisterSuccess()
            RegisterEvent.NavigateToLogin -> onNavigateToLogin()
            is RegisterEvent.Error -> Unit
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
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
        Spacer(Modifier.height(32.dp))
        Icon(BalkanEstateLogo, "Balkan Estate", Modifier.size(64.dp), tint = BalkanEstatePrimaryBlue)
        Spacer(Modifier.height(12.dp))
        Text("Create Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Join the Balkan Estate community", fontSize = 14.sp, color = BalkanEstateGray)

        Spacer(Modifier.height(24.dp))

        if (state.errorMessage != null) {
            Box(Modifier.fillMaxWidth().background(BalkanEstateRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(12.dp)) {
                Text(state.errorMessage, color = BalkanEstateRed, fontSize = 14.sp)
            }
            Spacer(Modifier.height(12.dp))
        }

        // Account type
        Text("I am a:", fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AccountType.entries.forEach { type ->
                FilterChip(
                    selected = state.accountType == type,
                    onClick = { onAction(RegisterAction.OnAccountTypeChanged(type)) },
                    label = { Text(type.displayName, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = state.firstName, onValueChange = { onAction(RegisterAction.OnFirstNameChanged(it)) }, label = { Text("First Name") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
            OutlinedTextField(value = state.lastName, onValueChange = { onAction(RegisterAction.OnLastNameChanged(it)) }, label = { Text("Last Name") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = state.email, onValueChange = { onAction(RegisterAction.OnEmailChanged(it)) }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = state.phone, onValueChange = { onAction(RegisterAction.OnPhoneChanged(it)) }, label = { Text("Phone Number (optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.password, onValueChange = { onAction(RegisterAction.OnPasswordChanged(it)) }, label = { Text("Password") },
            trailingIcon = { IconButton(onClick = { onAction(RegisterAction.OnTogglePasswordVisibility) }) { Icon(if (state.isPasswordVisible) EyeOpenedIcon else EyeClosedIcon, null, Modifier.size(20.dp)) } },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue),
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.confirmPassword, onValueChange = { onAction(RegisterAction.OnConfirmPasswordChanged(it)) }, label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue),
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), singleLine = true
        )

        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.agreeToTerms, onCheckedChange = { onAction(RegisterAction.OnToggleAgreeToTerms) }, colors = CheckboxDefaults.colors(checkedColor = BalkanEstatePrimaryBlue))
            Text("I agree to the ", fontSize = 13.sp, color = BalkanEstateGray)
            Text("Terms & Conditions", fontSize = 13.sp, color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onAction(RegisterAction.OnRegisterClick) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("Create Account", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Text("Already have an account? ", color = BalkanEstateGray)
            Text("Sign In", color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onAction(RegisterAction.OnLoginClick) })
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    BalkanEstateTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
