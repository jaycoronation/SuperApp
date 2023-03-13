package com.alphafinancialplanning.model

data class VarianceAnalysisMicroListResponse(
    val message: String,
    val success: Int,
    val variance_analysis_micro: VarianceAnalysisMicro
) {
    data class VarianceAnalysisMicro(
        val list: List<VarianceAnalysis>,
        val total: Total
    ) {
        data class VarianceAnalysis(
            val asset_type: String,
            val existing_allocation: String,
            val recommended_allocation: String,
            val variance: String
        )

        data class Total(
            val asset_type: String,
            val existing_allocation: String,
            val recommended_allocation: String,
            val variance: String
        )
    }
}