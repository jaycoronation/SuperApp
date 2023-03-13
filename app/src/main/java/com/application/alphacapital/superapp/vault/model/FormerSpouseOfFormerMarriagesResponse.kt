package com.alphaestatevault.model

data class FormerSpouseOfFormerMarriagesResponse(
    val former_spouse_of_former_marriages: MutableList<FormerSpouseOfFormerMarriage> = mutableListOf(),
    val message: String ="",
    val success: Int =0 ,
    val total_count: String =""
)
{
    data class FormerSpouseOfFormerMarriage(
        val address: String ="",
        val former_spouse_id: String ="",
        val holder: String ="",
        var holder_id : String ="",
        val holder_name: String ="",
        val name: String ="",
        val obligation: String ="",
        val phone: String ="",
        val relationship: String ="",
        val timestamp: String ="",
        val user_id: String =""
    )
}
