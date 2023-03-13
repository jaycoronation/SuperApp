package com.alphafinancialplanning.model

data class ReturnOfRiskListResponse(
    val message: String,
    val return_of_risk: List<ReturnOfRisk>,
    val success: Int
) {
    data class ReturnOfRisk(

        val one_year: String,
        val three_year: String,
        val five_year: String,
        val range_of_return: String
    )
}