package com.alphaestatevault.model

data class AccountHolderListResponse(
    val holders: MutableList<Holder> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String =""
)

{
    data class Holder(
        val address: String ="",
        val email: String="",
        val holder: String="",
        val holder_id: String="",
        val name: String="",
        val phone: String="",
        val timestamp: String="",
        val user_id: String=""
    )
}

