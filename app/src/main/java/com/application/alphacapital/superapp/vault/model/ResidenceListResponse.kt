package com.alphaestatevault.model

data class ResidenceListResponse(
    val keys_to_residences: MutableList<KeysToResidence> = mutableListOf(),
    val message: String ="",
    val success: Int=0,
    val total_count: String=""
)
{
    data class KeysToResidence(
        val email: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val keys_to_residences_id: String="",
        val location: String="",
        val name: String="",
        val phone: String="",
        val timestamp: String="",
        val user_id: String=""
    )
}

