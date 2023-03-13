package com.alphaestatevault.model

data class InvestTrustAccountListResponse(
    val investment_trust_accounts: MutableList<InvestmentTrustAccount> = mutableListOf(),
    val message: String ="",
    val success: Int =0 ,
    val total_count: String =""
)
{
    data class InvestmentTrustAccount(
        val account_number: String ="",
        val address: String ="",
        val amount: String ="",
        val asset_name: String ="",
        val contact_person: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name: String ="",
        val institution: String ="",
        val investment_trust_account_id: String ="",
        val location_of_document: String ="",
        val notes: String ="",
        val nominee_name: String ="",
        val timestamp: String ="",
        val upload_doc: String ="",
        val user_id: String =""
    )
}
