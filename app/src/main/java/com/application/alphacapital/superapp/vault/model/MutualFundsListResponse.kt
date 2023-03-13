package com.alphaestatevault.model

data class MutualFundsListResponse(
    val message: String ="",
    val mutual_funds: MutableList<MutualFund> = mutableListOf(),
    val success: Int = 0,
    val total_count: String =""
)
{
    data class MutualFund(
        val account_number: String="",
        val address: String="",
        val amount: String="",
        val asset_name: String="",
        val contact_person: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val institution: String="",
        val location_of_document: String="",
        val mutual_funds_id: String="",
        val nominee_name: String="",
        val notes: String="",
        val timestamp: String="",
        val upload_doc: String="",
        val user_id: String=""
    )
}
