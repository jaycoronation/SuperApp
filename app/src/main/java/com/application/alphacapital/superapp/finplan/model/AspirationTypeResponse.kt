package com.alphafinancialplanning.model

data class AspirationTypeResponse(
    val aspiration_types: List<AspirationType>,
    val message: String,
    val success: Int
) {
    data class AspirationType(
        val aspiration_type: String,
        val aspiration_type_id: String
    )
}