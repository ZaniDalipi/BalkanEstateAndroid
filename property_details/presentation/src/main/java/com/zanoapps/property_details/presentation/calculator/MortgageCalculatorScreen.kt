package com.zanoapps.property_details.presentation.calculator

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MortgageCalculatorScreenRoot(
    viewModel: MortgageCalculatorViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    MortgageCalculatorScreen(
        state = viewModel.state,
        onPropertyPriceChanged = viewModel::onPropertyPriceChanged,
        onDownPaymentChanged = viewModel::onDownPaymentChanged,
        onDownPaymentPercentChanged = viewModel::onDownPaymentPercentChanged,
        onInterestRateChanged = viewModel::onInterestRateChanged,
        onLoanTermChanged = viewModel::onLoanTermChanged,
        getRemainingBalance = viewModel::getRemainingBalance,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MortgageCalculatorScreen(
    state: MortgageCalculatorState,
    onPropertyPriceChanged: (String) -> Unit,
    onDownPaymentChanged: (String) -> Unit,
    onDownPaymentPercentChanged: (Float) -> Unit,
    onInterestRateChanged: (String) -> Unit,
    onLoanTermChanged: (Int) -> Unit,
    getRemainingBalance: (Int) -> Double,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mortgage Calculator",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input Card
            InputCard(
                state = state,
                onPropertyPriceChanged = onPropertyPriceChanged,
                onDownPaymentChanged = onDownPaymentChanged,
                onDownPaymentPercentChanged = onDownPaymentPercentChanged,
                onInterestRateChanged = onInterestRateChanged,
                onLoanTermChanged = onLoanTermChanged
            )

            // Results Card
            ResultsCard(state = state)

            // Payment Breakdown
            PaymentBreakdownCard(state = state)

            // Amortization Summary
            AmortizationSummaryCard(
                state = state,
                getRemainingBalance = getRemainingBalance
            )

            // Apply for Mortgage Button
            Button(
                onClick = { /* Apply for mortgage action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstateOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Apply for Mortgage",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InputCard(
    state: MortgageCalculatorState,
    onPropertyPriceChanged: (String) -> Unit,
    onDownPaymentChanged: (String) -> Unit,
    onDownPaymentPercentChanged: (Float) -> Unit,
    onInterestRateChanged: (String) -> Unit,
    onLoanTermChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Loan Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            // Property Price
            OutlinedTextField(
                value = state.propertyPrice,
                onValueChange = onPropertyPriceChanged,
                label = { Text("Property Price") },
                prefix = { Text("€ ", color = BalkanEstateGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    cursorColor = BalkanEstatePrimaryBlue,
                    focusedLabelColor = BalkanEstatePrimaryBlue
                ),
                singleLine = true
            )

            // Down Payment
            OutlinedTextField(
                value = state.downPayment,
                onValueChange = onDownPaymentChanged,
                label = { Text("Down Payment") },
                prefix = { Text("€ ", color = BalkanEstateGray) },
                suffix = {
                    Text(
                        text = "(${state.downPaymentPercent.toInt()}%)",
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    cursorColor = BalkanEstatePrimaryBlue,
                    focusedLabelColor = BalkanEstatePrimaryBlue
                ),
                singleLine = true
            )

            // Down Payment Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "10%",
                        fontSize = 12.sp,
                        color = BalkanEstateGray
                    )
                    Text(
                        text = "50%",
                        fontSize = 12.sp,
                        color = BalkanEstateGray
                    )
                }
                Slider(
                    value = state.downPaymentPercent,
                    onValueChange = onDownPaymentPercentChanged,
                    valueRange = 10f..50f,
                    steps = 7,
                    colors = SliderDefaults.colors(
                        thumbColor = BalkanEstatePrimaryBlue,
                        activeTrackColor = BalkanEstatePrimaryBlue,
                        inactiveTrackColor = BalkanEstatePrimaryBlue.copy(alpha = 0.2f)
                    )
                )
            }

            // Interest Rate
            OutlinedTextField(
                value = state.interestRate,
                onValueChange = onInterestRateChanged,
                label = { Text("Interest Rate") },
                suffix = { Text("% ", color = BalkanEstateGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    cursorColor = BalkanEstatePrimaryBlue,
                    focusedLabelColor = BalkanEstatePrimaryBlue
                ),
                singleLine = true
            )

            // Loan Term Selector
            LoanTermSelector(
                selectedYears = state.loanTermYears,
                onYearsSelected = onLoanTermChanged
            )
        }
    }
}

@Composable
private fun LoanTermSelector(
    selectedYears: Int,
    onYearsSelected: (Int) -> Unit
) {
    val loanTermOptions = listOf(5, 10, 15, 20, 25, 30)
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Loan Term",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box {
            OutlinedTextField(
                value = "$selectedYears years",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    disabledBorderColor = Color(0xFFBDBDBD),
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false
            )
            // Transparent clickable overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                loanTermOptions.forEach { years ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$years years",
                                fontWeight = if (years == selectedYears) FontWeight.Bold else FontWeight.Normal,
                                color = if (years == selectedYears) BalkanEstatePrimaryBlue else Color.DarkGray
                            )
                        },
                        onClick = {
                            onYearsSelected(years)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultsCard(state: MortgageCalculatorState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Monthly Payment",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(state.monthlyPayment),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            ResultRow(label = "Total Payment", value = formatCurrency(state.totalPayment))
            Spacer(modifier = Modifier.height(8.dp))
            ResultRow(label = "Total Interest", value = formatCurrency(state.totalInterest))
            Spacer(modifier = Modifier.height(8.dp))
            ResultRow(label = "Loan Amount", value = formatCurrency(state.loanAmount))
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun PaymentBreakdownCard(state: MortgageCalculatorState) {
    val total = state.totalPayment
    val principalRatio = if (total > 0) (state.loanAmount / total).toFloat() else 0f
    val interestRatio = if (total > 0) (state.totalInterest / total).toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Payment Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Colored bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (principalRatio > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(principalRatio)
                            .height(24.dp)
                            .background(BalkanEstatePrimaryBlue)
                    )
                }
                if (interestRatio > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(interestRatio)
                            .height(24.dp)
                            .background(BalkanEstateOrange)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(
                    color = BalkanEstatePrimaryBlue,
                    label = "Principal",
                    value = formatCurrency(state.loanAmount)
                )
                LegendItem(
                    color = BalkanEstateOrange,
                    label = "Interest",
                    value = formatCurrency(state.totalInterest)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = BalkanEstateGray
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun AmortizationSummaryCard(
    state: MortgageCalculatorState,
    getRemainingBalance: (Int) -> Double
) {
    val milestoneYears = listOf(1, 5, 10).filter { it <= state.loanTermYears }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Amortization Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))

            milestoneYears.forEachIndexed { index, year ->
                val remainingBalance = getRemainingBalance(year)
                val paidOff = state.loanAmount - remainingBalance
                val paidPercent = if (state.loanAmount > 0) (paidOff / state.loanAmount * 100) else 0.0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Year $year",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.1f", paidPercent)}% paid off",
                            fontSize = 12.sp,
                            color = BalkanEstateGreen
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Remaining",
                            fontSize = 12.sp,
                            color = BalkanEstateGray
                        )
                        Text(
                            text = formatCurrency(remainingBalance),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                    }
                }

                if (index < milestoneYears.lastIndex) {
                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return "€${formatter.format(amount)}"
}

@Preview(showBackground = true)
@Composable
private fun MortgageCalculatorScreenPreview() {
    BalkanEstateTheme {
        MortgageCalculatorScreen(
            state = MortgageCalculatorState(
                propertyPrice = "200000",
                downPayment = "40000",
                downPaymentPercent = 20f,
                interestRate = "3.5",
                loanTermYears = 25,
                monthlyPayment = 800.45,
                totalPayment = 240135.0,
                totalInterest = 80135.0,
                loanAmount = 160000.0
            ),
            onPropertyPriceChanged = {},
            onDownPaymentChanged = {},
            onDownPaymentPercentChanged = {},
            onInterestRateChanged = {},
            onLoanTermChanged = {},
            getRemainingBalance = { year ->
                when (year) {
                    1 -> 155800.0
                    5 -> 138500.0
                    10 -> 109200.0
                    else -> 0.0
                }
            },
            onNavigateBack = {}
        )
    }
}
