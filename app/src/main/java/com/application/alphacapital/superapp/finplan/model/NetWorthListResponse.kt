package com.alphafinancialplanning.model

data class NetWorthListResponse(
    val message: String,
    val networth: Networth,
    val success: Int
) {
    data class Networth(
        val list: List<NetWorthList>,
        val total: Total
    ) {
        data class NetWorthList(
            val asset_type: String,
            val current_value: String,
            val investment_type: String
        )

        data class Total(
            val current_value: String
        )
    }
}