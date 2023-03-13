package com.alphafinancialplanning.model

data class RiskProfileAllocationResponse(
    val message: String,
    val risk_profile_allocation: List<RiskProfileAllocation>,
    val success: Int
) {
    data class RiskProfileAllocation(
        val allocation: String,
        val asset_class: String,
        val expected_return: String
    )
}