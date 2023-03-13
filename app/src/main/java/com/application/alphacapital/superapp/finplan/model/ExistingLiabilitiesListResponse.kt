package com.alphafinancialplanning.model

data class ExistingLiabilitiesListResponse(
    val existing_liabilities: List<ExistingLiability>,
    val message: String,
    val success: Int,
    val total_count: String
) {
    data class ExistingLiability(
        val asset_type: String,
        val current_value: String,
        val existing_liability_id: String,
        val liability_type: String,
        val timestamp: String,
        val user_id: String
    )
}