package com.zanoapps.property_details.presentation.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class MortgageCalculatorViewModel : ViewModel() {

    var state by mutableStateOf(MortgageCalculatorState())
        private set

    init {
        recalculate()
    }

    fun onPropertyPriceChanged(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        state = state.copy(propertyPrice = filtered)
        updateDownPaymentFromPercent()
        recalculate()
    }

    fun onDownPaymentChanged(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        val price = state.propertyPrice.toDoubleOrNull() ?: 0.0
        val downPaymentValue = filtered.toDoubleOrNull() ?: 0.0
        val percent = if (price > 0) ((downPaymentValue / price) * 100).toFloat().coerceIn(0f, 100f) else 0f
        state = state.copy(
            downPayment = filtered,
            downPaymentPercent = percent
        )
        recalculate()
    }

    fun onDownPaymentPercentChanged(value: Float) {
        val price = state.propertyPrice.toDoubleOrNull() ?: 0.0
        val downPaymentValue = (price * value / 100).toLong()
        state = state.copy(
            downPaymentPercent = value,
            downPayment = downPaymentValue.toString()
        )
        recalculate()
    }

    fun onInterestRateChanged(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        state = state.copy(interestRate = filtered)
        recalculate()
    }

    fun onLoanTermChanged(years: Int) {
        state = state.copy(loanTermYears = years)
        recalculate()
    }

    private fun updateDownPaymentFromPercent() {
        val price = state.propertyPrice.toDoubleOrNull() ?: 0.0
        val downPaymentValue = (price * state.downPaymentPercent / 100).toLong()
        state = state.copy(downPayment = downPaymentValue.toString())
    }

    private fun recalculate() {
        val price = state.propertyPrice.toDoubleOrNull() ?: 0.0
        val downPayment = state.downPayment.toDoubleOrNull() ?: 0.0
        val annualRate = state.interestRate.toDoubleOrNull() ?: 0.0
        val years = state.loanTermYears

        val loanAmount = (price - downPayment).coerceAtLeast(0.0)

        if (loanAmount <= 0 || annualRate <= 0 || years <= 0) {
            state = state.copy(
                loanAmount = loanAmount,
                monthlyPayment = 0.0,
                totalPayment = 0.0,
                totalInterest = 0.0
            )
            return
        }

        // M = P[r(1+r)^n]/[(1+r)^n - 1]
        val monthlyRate = annualRate / 100.0 / 12.0
        val numberOfPayments = years * 12.0
        val compoundFactor = (1 + monthlyRate).pow(numberOfPayments)

        val monthlyPayment = loanAmount * (monthlyRate * compoundFactor) / (compoundFactor - 1)
        val totalPayment = monthlyPayment * numberOfPayments
        val totalInterest = totalPayment - loanAmount

        state = state.copy(
            loanAmount = loanAmount,
            monthlyPayment = monthlyPayment,
            totalPayment = totalPayment,
            totalInterest = totalInterest
        )
    }

    fun getRemainingBalance(afterYears: Int): Double {
        val annualRate = state.interestRate.toDoubleOrNull() ?: return 0.0
        if (annualRate <= 0 || state.loanAmount <= 0) return 0.0

        val monthlyRate = annualRate / 100.0 / 12.0
        val totalMonths = state.loanTermYears * 12
        val paidMonths = (afterYears * 12).coerceAtMost(totalMonths)

        val compoundTotal = (1 + monthlyRate).pow(totalMonths.toDouble())
        val compoundPaid = (1 + monthlyRate).pow(paidMonths.toDouble())

        // Remaining balance = P * [(1+r)^n - (1+r)^p] / [(1+r)^n - 1]
        val remaining = state.loanAmount * (compoundTotal - compoundPaid) / (compoundTotal - 1)
        return remaining.coerceAtLeast(0.0)
    }
}
