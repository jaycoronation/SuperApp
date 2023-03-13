package com.alphafinancialplanning.model

data class WealthRequiredListResponse(
    val message: String,
    val success: Int,
    val wealth_required: List<WealthRequired>,
    val total_wealth:Double
) {
    data class WealthRequired(
        val exisiting_wealth: String,
        val existing: String,
        val future_wealth: String,
        val human_value: String,
        val wealth_required: String,
        val year: String
    )
}