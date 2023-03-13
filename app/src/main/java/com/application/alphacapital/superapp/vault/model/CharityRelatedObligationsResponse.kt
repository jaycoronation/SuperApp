package com.alphaestatevault.model

data class CharityRelatedObligationsResponse(
    val charity_related_obligations: MutableList<CharityRelatedObligation> = mutableListOf(),
    val message: String ="",
    val success: Int=0,
    val total_count: String =""
)
{
    data class CharityRelatedObligation(
        val address: String ="",
        val charity_related_obligation_id: String ="",
        val contact: String ="",
        val holder: String ="",
        val holder_id : String ="",
        val holder_name: String ="",
        val instructions: String ="",
        val nature_of_obligation: String ="",
        val organization: String ="",
        val phone: String ="",
        val timestamp: String ="",
        val user_id: String =""
    )
}

