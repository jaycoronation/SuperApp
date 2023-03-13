package com.alphaestatevault.model

data class InsurancePoliciesListResponse(
    val insurance_policies_details: MutableList<InsurancePoliciesDetail> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String =""
)
{
    data class InsurancePoliciesDetail(
        val agent_address: String ="",
        val agent_name: String ="",
        val agent_phone: String ="",
        val current_value: String ="",
        val holder: String ="",
        val holder_id: String ="",
        val holder_name: String ="",
        val insurance_company: String ="",
        val nominee_name : String ="",
        val insurance_policies_id: String ="",
        val location_of_document: String ="",
        val notes: String ="",
        val person_thing_insured: String ="",
        val policy_number: String ="",
        val purchased_on: String ="",
        val sum_assured: String ="",
        val timestamp: String ="",
        val type_of_policy: String ="",
        val upload_doc: String ="",
        val user_id: String =""
    )
}

