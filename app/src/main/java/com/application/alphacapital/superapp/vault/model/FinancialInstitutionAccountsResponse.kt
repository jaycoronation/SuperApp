package com.alphaestatevault.model

data class FinancialInstitutionAccountsResponse(
    val financial_institution_accounts: MutableList<FinancialInstitutionAccount> = mutableListOf(),
    val message: String ="",
    val success: Int=0,
    val total_count: String=""
)
{
    data class FinancialInstitutionAccount(
        val account_number_and_type: String="",
        val approximate_value: String="",
        val bank_name: String="",
        val branch: String="",
        val financial_institution_account_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val nominee_name : String ="",
        val other_than_own_name: String="",
        val timestamp: String="",
        val upload_doc: String="",
        var notes :String = "",
        val user_id: String=""
    )
}

