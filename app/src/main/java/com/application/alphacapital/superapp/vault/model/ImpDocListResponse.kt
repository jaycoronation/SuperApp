package com.alphaestatevault.model

data class ImpDocListResponse(
    val documents: MutableList<Document> = mutableListOf(),
    val message: String ="",
    val success: Int =0,
    val total_count: String =""
)
{
    data class Document(
        val document: String="",
        val document_id: String="",
        val holder: String="",
        val holder_id: String="",
        val holder_name: String="",
        val location: String="",
        val softcopy: String="",
        val timestamp: String="",
        val user_id: String=""
    )
}
