package com.alphaestatevault.model

data class OtherAssetsResponse(
    val message: String ="",
    val other_assets: MutableList<OtherAsset> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class OtherAsset(
        val account_number: String ="",
        val amount: String ="",
        val approximate_value: String ="",
        val asset_name: String ="",
        val contact_person: String ="",
        val description: String ="",
        val encumbrances: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name: String ="",
        val nominee_name : String ="",
        val institution: String ="",
        val location_of_document: String ="",
        val notes: String ="",
        val other_asset_id: String ="",
        val timestamp: String ="",
        val upload_doc: String ="",
        val user_id: String =""
    )
}
