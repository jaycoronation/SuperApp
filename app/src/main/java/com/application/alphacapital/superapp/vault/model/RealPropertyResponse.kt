package com.alphaestatevault.model

data class RealPropertyResponse(
    val message: String ="",
    val real_properties: MutableList<RealProperty> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class RealProperty(
        val approximate_value: String ="",
        val description: String ="",
        val name : String ="",
        val encumbrances: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name: String ="",
        val loan: String ="",
        val location: String ="",
        val location_of_document: String ="",
        val nominee_name : String ="",
        val notes: String ="",
        val purchased_on: String ="",
        val real_property_id: String ="",
        val rent: String ="",
        val timestamp: String ="",
        val title: String ="",
        val type_of_property: String ="",
        val upload_doc: String ="",
        val user_id: String =""
    )
}
