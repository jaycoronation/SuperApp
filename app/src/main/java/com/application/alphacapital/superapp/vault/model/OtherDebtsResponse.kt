package com.alphaestatevault.model

data class OtherDebtsResponse(
    val message: String ="",
    val other_debts_details: MutableList<OtherDebtsDetail> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class OtherDebtsDetail(
        val address: String="",
        val contact: String="",
        val created_on: String="",
        val currently_outstanding: String="",
        val description: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val notes: String="",
        val other_debt_id: String="",
        val phone: String="",
        val terms: String="",
        val timestamp: String="",
        val to_whom_owed: String="",
        val upload_doc: String="",
        val user_id: String=""
    )
}
