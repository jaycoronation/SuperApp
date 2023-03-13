package com.alphafinancialplanning.model

data class LiabilitiesTypeResponse(
    val liabilities: List<Liability>,
    val message: String,
    val success: Int
) {
    data class Liability(
        val asset_type: String,
        val liability: String,
        val liability_id: String
    )
}