package com.alphafinancialplanning.model

data class BalanceSheetMovementListResponse(
    val balance_sheet_movement: List<BalanceSheetMovement>,
    val message: String,
    val success: Int
) {
    data class BalanceSheetMovement(
        val closing_balance: String,
        val growth: String,
        val inflow: String,
        val opening_balance: String = "",
        val outflow: String,
        val year: String,
        val closing_pv:String
    )
}