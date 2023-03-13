package com.alphafinancialplanning.model

data class InvestmentTypeResponse(
    val investment_types: List<InvestmentType>,
    val message: String,
    val success: Int
) {
    data class InvestmentType(
        val asset_type: String,
        val investment_type: String,
        val investment_type_id: String
    )
}