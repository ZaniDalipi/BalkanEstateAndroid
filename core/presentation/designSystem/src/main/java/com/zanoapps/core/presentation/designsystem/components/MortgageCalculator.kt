package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import kotlin.math.pow

@Composable
fun MortgageCalculator(
    propertyPrice: Double,
    onApplyForMortgage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var downPaymentPercent by remember { mutableFloatStateOf(20f) }
    var interestRate by remember { mutableStateOf("3.5") }
    var loanTermYears by remember { mutableFloatStateOf(25f) }

    val downPaymentAmount by remember(propertyPrice, downPaymentPercent) {
        derivedStateOf { propertyPrice * (downPaymentPercent / 100) }
    }

    val loanAmount by remember(propertyPrice, downPaymentAmount) {
        derivedStateOf { propertyPrice - downPaymentAmount }
    }

    val monthlyPayment by remember(loanAmount, interestRate, loanTermYears) {
        derivedStateOf {
            calculateMonthlyPayment(
                principal = loanAmount,
                annualInterestRate = interestRate.toDoubleOrNull() ?: 3.5,
                years = loanTermYears.toInt()
            )
        }
    }

    val totalPayment by remember(monthlyPayment, loanTermYears) {
        derivedStateOf { monthlyPayment * loanTermYears.toInt() * 12 }
    }

    val totalInterest by remember(totalPayment, loanAmount) {
        derivedStateOf { totalPayment - loanAmount }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = HomeIcon,
                        contentDescription = null,
                        tint = BalkanEstatePrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mortgage Calculator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "Estimate your monthly payment",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Property Price Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Property Price",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "€${"%,.0f".format(propertyPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Down Payment Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Down Payment",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "${downPaymentPercent.toInt()}% (€${"%,.0f".format(downPaymentAmount)})",
                        fontSize = 14.sp,
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = downPaymentPercent,
                    onValueChange = { downPaymentPercent = it },
                    valueRange = 5f..50f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = BalkanEstatePrimaryBlue,
                        activeTrackColor = BalkanEstatePrimaryBlue,
                        inactiveTrackColor = Color.LightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interest Rate Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Interest Rate (%)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = interestRate,
                        onValueChange = { interestRate = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BalkanEstatePrimaryBlue,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        suffix = { Text("%", color = Color.Gray) }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Loan Term",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "${loanTermYears.toInt()}",
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BalkanEstatePrimaryBlue,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        readOnly = true,
                        singleLine = true,
                        suffix = { Text("years", color = Color.Gray) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Loan Term Slider
            Slider(
                value = loanTermYears,
                onValueChange = { loanTermYears = it },
                valueRange = 5f..30f,
                steps = 4,
                colors = SliderDefaults.colors(
                    thumbColor = BalkanEstatePrimaryBlue,
                    activeTrackColor = BalkanEstatePrimaryBlue,
                    inactiveTrackColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(20.dp))

            // Results
            ResultRow(
                label = "Loan Amount",
                value = "€${"%,.0f".format(loanAmount)}"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ResultRow(
                label = "Total Interest",
                value = "€${"%,.0f".format(totalInterest)}"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ResultRow(
                label = "Total Payment",
                value = "€${"%,.0f".format(totalPayment)}"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Monthly Payment Highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        BalkanEstateGreen.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estimated Monthly Payment",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "€${"%,.0f".format(monthlyPayment)}/mo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BalkanEstateGreen,
                        fontFamily = Poppins
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Apply Button
            Button(
                onClick = onApplyForMortgage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                )
            ) {
                Text(
                    text = "Get Pre-Approved",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* This is an estimate. Actual rates may vary.",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ResultRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}

private fun calculateMonthlyPayment(
    principal: Double,
    annualInterestRate: Double,
    years: Int
): Double {
    if (principal <= 0 || annualInterestRate <= 0 || years <= 0) return 0.0

    val monthlyRate = annualInterestRate / 100 / 12
    val numberOfPayments = years * 12

    return principal * (monthlyRate * (1 + monthlyRate).pow(numberOfPayments)) /
            ((1 + monthlyRate).pow(numberOfPayments) - 1)
}

@Preview(showBackground = true)
@Composable
private fun MortgageCalculatorPreview() {
    BalkanEstateTheme {
        MortgageCalculator(
            propertyPrice = 250000.0,
            onApplyForMortgage = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
