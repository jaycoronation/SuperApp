package com.alphaestatevault.model

data class AssetsNotPossessionListResponse(
    val assets_not_in_possessions: List<AssetsNotInPossession>,
    val message: String,
    val success: Int,
    val total_count: String
) {
    data class AssetsNotInPossession(
        val approximate_value: String,
        val description: String,
        val documentation: String,
        val holder: String,
        val location_of_documentation: String,
        val notes: String,
        val possession_id: String,
        val timestamp: String,
        val title: String,
        val user_id: String
    )
}