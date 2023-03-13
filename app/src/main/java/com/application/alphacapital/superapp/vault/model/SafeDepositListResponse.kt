package com.alphaestatevault.model

data class SafeDepositListResponse(
    val message: String ="",
    val safe_deposit_boxes: MutableList<SafeDepositBoxe> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class SafeDepositBoxe(
        val box_number: String="",
        val general_inventory: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val location: String="",
        val person_authorized_to_sign: String="",
        val person_with_keys: String="",
        val safe_deposit_box_id: String="",
        val timestamp: String="",
        val user_id: String=""
    )
}

