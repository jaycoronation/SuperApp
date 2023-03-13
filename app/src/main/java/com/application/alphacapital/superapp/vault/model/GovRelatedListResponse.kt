package com.alphaestatevault.model

data class GovRelatedListResponse(
    val government_related_details: MutableList<GovernmentRelatedDetail> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String=""
)
{
    data class GovernmentRelatedDetail(
        val account_number: String="",
        val address: String="",
        val amount: String="",
        val asset_name: String="",
        val contact_person: String="",
        val government_related_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val institution: String="",
        val nominee_name : String ="",
        val location_of_document: String="",
        val notes: String="",
        val timestamp: String="",
        val upload_doc: String="",
        val user_id: String=""
    )
}

