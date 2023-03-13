package com.alphaestatevault.model

data class CreditCardsAndLoansResponse(
    val credit_cards_and_loans: MutableList<CreditCardsAndLoan> = mutableListOf(),
    val message: String ="",
    val success: Int=0,
    val total_count: String=""
)

{
    data class CreditCardsAndLoan(
        val account_number: String="",
        val amount: String="",
        val contact_person: String="",
        val created_on: String="",
        val credit_cards_and_loan_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val institution: String="",
        val location_of_document: String="",
        val notes: String="",
        val timestamp: String="",
        val type: String="",
        val upload_doc: String="",
        val user_id: String=""
    )
}
