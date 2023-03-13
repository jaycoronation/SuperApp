package com.alphafinancialplanning.model

data class AddRecommendedAllocation(
    val recommended_asset_id:String = "",
    val asset_class: String = "",
    var allocation: String = "",
    var return_expectation: String = ""
)