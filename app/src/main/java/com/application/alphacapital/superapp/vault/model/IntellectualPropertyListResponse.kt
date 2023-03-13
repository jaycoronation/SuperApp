package com.alphaestatevault.model

data class IntellectualPropertyListResponse(
    val intellectual_properties: MutableList<IntellectualProperty> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String=""
)
{
    data class IntellectualProperty(
        val address: String="",
        val branch: String="",
        val created_on: String="",
        val description: String="",
        val firm: String="",
        val holder: String="",
        val holder_id: String="",
        val nominee_name : String ="",
        val holder_name: String="",
        val intellectual_property_id: String="",
        val lawyer_name: String="",
        val location_of_document: String="",
        val notes: String="",
        val phone: String="",
        val timestamp: String="",
        val upload_doc: String="",
        val user_id: String=""
    )
}
