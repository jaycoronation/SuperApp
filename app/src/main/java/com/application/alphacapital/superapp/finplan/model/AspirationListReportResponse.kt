package com.alphafinancialplanning.model

data class AspirationListReportResponse(
    val aspirations: Aspirations,
    val message: String,
    val success: Int
) {
    data class Aspirations(
        val list: List<AspirationList>,
        val total: Total
    ) {
        data class AspirationList(
            val aspiration_id: String,
            val aspiration_type: String,
            val end_year: String,
            val start_year: String,
            val target_return: String,
            val total_inflation_adjusted_expense: String,
            val total_outflow: String,
            val user_id: String,
            val volatile_component: String,
            val wealth_required_today_total: String
        )

        data class Total(
            val inflation_adjusted_outflow: String,
            val target_return: String,
            val total_outflow: String,
            val volatile_component: String,
            val wealth_required_today_total: String
        )
    }
}