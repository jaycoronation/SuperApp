package com.alphafinancialplanning.model

data class AssetAllocationMacroListResponse(
    val asset_allocation_macro: AssetAllocationMacro,
    val message: String,
    val success: Int
) {
    data class AssetAllocationMacro(
        val list: List<AssetAllocationMacroList>,
        val total: Total
    ) {
        data class AssetAllocationMacroList(
            val allocation: String,
            val amount: String,
            val asset_class: String,
            val `return`: String,
            val risk: String
        )

        data class Total(
            val allocation: String,
            val amount: String,
            val asset_class: String,
            val `return`: String,
            val risk: String
        )
    }
}