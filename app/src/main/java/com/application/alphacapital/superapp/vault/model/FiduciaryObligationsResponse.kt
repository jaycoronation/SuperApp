package com.alphaestatevault.model

data class FiduciaryObligationsResponse(
    val fiduciary_obligations: MutableList<FiduciaryObligation> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String=""
)
{
    data class FiduciaryObligation(
        val address: String="",
        val contact: String="",
        val created_on: String="",
        val fiduciary_obligation_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val instructions: String="",
        val name: String="",
        val notes: String="",
        val phone: String="",
        val relationship: String="",
        val timestamp: String="",
        val types_of_records: String="",
        val user_id: String=""
    )
}
