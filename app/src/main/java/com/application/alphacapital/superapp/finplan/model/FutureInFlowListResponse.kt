package com.alphafinancialplanning.model

data class FutureInFlowListResponse(
    val future_inflows: List<FutureInflow>,
    val message: String,
    val success: Int,
    val total_count: String
) {
    data class FutureInflow(
        val amount: String,
        val end_year: String,
        val expected_growth: String,
        val future_inflow_id: String,
        val source: String,
        val start_year: String,
        val timestamp: String,
        val user_id: String
    )
}