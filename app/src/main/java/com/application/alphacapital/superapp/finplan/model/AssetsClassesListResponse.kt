package com.alphafinancialplanning.model

data class AssetsClassesListResponse(
    val assets_classes: List<String>,
    val message: String,
    val success: Int
)