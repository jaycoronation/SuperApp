package com.application.alphacapital.superapp.finplan.model

data class RecommendedAssetAllocationListResponse(
    val message: String,
    val recommended_asset_allocation: RecommendedAssetAllocation,
    val success: Int
) {
    data class RecommendedAssetAllocation(
        val list: List<AssetList>,
        val total: Total
    ) {
        data class AssetList(
            val allocation: String,
            val amount: String,
            val asset_class: String,
            val return_expectation: String,
            val recommended_asset_id:String
        )

        data class Total(
            val allocation: String,
            val amount: String,
            val asset_class: String,
            val `return`: String
        )
    }
}