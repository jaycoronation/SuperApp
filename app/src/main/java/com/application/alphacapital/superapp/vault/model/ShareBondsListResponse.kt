package com.alphaestatevault.model

data class ShareBondsListResponse(
    val message: String ="",
    val shares_bonds: MutableList<SharesBond> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class SharesBond(
        val account_number: String ="",
        val address: String ="",
        val amount: String ="",
        val asset_name: String ="",
        val contact_person: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name: String ="",
        val institution: String ="",
        val location_of_document: String ="",
        val nominee_name: String ="",
        val notes: String ="",
        val shares_bonds_id: String ="",
        val timestamp: String ="",
        val upload_doc: String ="",
        val user_id: String =""
    )
}
