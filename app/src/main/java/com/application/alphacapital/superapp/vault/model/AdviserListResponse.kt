package com.alphaestatevault.model

data class AdviserListResponse(
    val advisers: MutableList<Adviser> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String =""
)
{
    data class Adviser(
    val address: String="",
    val adviser_id: String="",
    val company: String="",
    val holder: String="",
    val holder_id: String="",
    val holder_name: String="",
    val name: String="",
    val phone: String="",
    val relationship: String="",
    val timestamp: String="",
    val user_id: String="")
}