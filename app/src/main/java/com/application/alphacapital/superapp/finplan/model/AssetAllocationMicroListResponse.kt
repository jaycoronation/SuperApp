package com.application.alphacapital.superapp.finplan.model

data class AssetAllocationMicroListResponse(
    val message: String = "",
    val asset_allocation_micro: AssetAllocationMicro = AssetAllocationMicro(),
    val success: Int = 0
) {
    data class AssetAllocationMicro(
        val list: List<AssetAllocationMicroList> = listOf(),
        val total: Total = Total()
    ) {
        data class AssetAllocationMicroList(
            val amount: String = "",
            val asset_type: String = "",
            val allocation: String = ""
        )

        data class Total(
            val allocation: String = "",
            val amount: String = "",
            val asset_type: String = ""
        )
    }
}