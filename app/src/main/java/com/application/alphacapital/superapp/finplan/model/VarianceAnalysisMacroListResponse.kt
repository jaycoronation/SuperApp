package com.alphafinancialplanning.model

data class VarianceAnalysisMacroListResponse(
    val message: String,
    val success: Int,
    val variance_analysis_macro: VarianceAnalysisMacro
) {
    data class VarianceAnalysisMacro(
        val list: List<VarianceAnalysis>,
        val total: Total
    ) {
        data class VarianceAnalysis(
            val asset_class: String,
            val existing_allocation: String,
            val recommended_allocation: String,
            val `return`: String,
            val risk: String,
            val variance: String
        )

        data class Total(
            val asset_class: String,
            val existing_allocation: String,
            val recommended_allocation: String,
            val `return`: String,
            val risk: String,
            val variance: String
        )
    }
}