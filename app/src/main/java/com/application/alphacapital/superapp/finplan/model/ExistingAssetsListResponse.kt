package com.application.alphacapital.superapp.finplan.model

data class ExistingAssetsListResponse(
    val existing_assets: List<ExistingAsset>,
    val message: String,
    val success: Int,
    val total_count: String
) {
    data class ExistingAsset(
        val asset_type: String,
        val current_value: String,
        val existing_assets_id: String,
        val investment_type: String,
        val timestamp: String,
        val user_id: String,
        val can_delete: Int
    )
}