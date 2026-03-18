package com.zanoapps.property_details.presentation.calculator

data class MortgageCalculatorState(
    val propertyPrice: String = "200000",
    val downPayment: String = "40000",
    val downPaymentPercent: Float = 20f,
    val interestRate: String = "3.5",
    val loanTermYears: Int = 25,
    val monthlyPayment: Double = 0.0,
    val totalPayment: Double = 0.0,
    val totalInterest: Double = 0.0,
    val loanAmount: Double = 0.0
)
