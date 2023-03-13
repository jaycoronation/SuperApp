package com.alphafinancialplanning.model

data class FutureInflowListReportResponse(
    val future_inflow: FutureInflow,
    val message: String,
    val success: Int
) {
    data class FutureInflow(
        val list: List<FutureInflowList>,
        val total: Total
    ) {
        data class FutureInflowList(
            val end_year: String,
            val expected_growth: String,
            val future_inflow_id: String,
            val inflation_adjusted_income: String,
            val pv_of_income: String,
            val source: String,
            val start_year: String,
            val user_id: String,
            val amount:String
        )

        data class Total(
            val inflation_adjusted_income: String,
            val pv_of_income: String
        )
    }
}