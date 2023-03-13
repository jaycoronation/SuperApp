package com.alphaestatevault.model

data class BusinessDetailResponse(
    val business: Business,
    val message: String ="",
    val success: Int =0
)
{
    data class Business(
        val businesses_details: MutableList<BusinessesDetail> = mutableListOf(),
        val businesses_id: String ="",
        val document_instructions: String ="",
        val document_stating_your_wishes: String ="",
        val own_or_jointly_business: String ="",
        val timestamp: String ="",
        val user_id: String =""
    )

    data class BusinessesDetail(
        val area_of_business: String ="",
        val business_detail_id: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name : String ="",
        val name: String ="",
        val timestamp: String ="",
        val type_of_organization: String ="",
        val user_id: String =""
    )
}

