package com.alphaestatevault.model

data class ConstitutionListResponse(
    val data: MutableList<Data> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String =""
)
{
    data class Data(
        val id: String ="",
        val notes: String ="",
        val timestamp: String ="",
        val user_id: String=""
    )
}
