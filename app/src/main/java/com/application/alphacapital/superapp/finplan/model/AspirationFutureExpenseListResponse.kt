package com.alphafinancialplanning.model

data class AspirationFutureExpenseListResponse(
    val aspiration_future_expenses: List<AspirationFutureExpense>,
    val message: String,
    val success: Int,
    val total_count: String
) {
    data class AspirationFutureExpense(
        val amount: String,
        val aspiration_id: String,
        val aspiration_type: String,
        val end_year: String,
        val periodicity: String,
        val start_year: String,
        val timestamp: String,
        val user_id: String
    )
}