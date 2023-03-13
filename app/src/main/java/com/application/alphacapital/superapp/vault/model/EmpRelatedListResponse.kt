package com.alphaestatevault.model

data class EmpRelatedListResponse(
    val employment_related_details: MutableList<EmploymentRelatedDetail> = mutableListOf(),
    val message: String ="",
    val success: Int = 0,
    val total_count: String=""
)
{
    data class EmploymentRelatedDetail(
        val address: String="",
        val company: String="",
        val contact: String="",
        val employment_related_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val nominee_name : String ="",
        val phone: String="",
        val timestamp: String="",
        val type_nature_amount: String="",
        val user_id: String=""
    )
}

