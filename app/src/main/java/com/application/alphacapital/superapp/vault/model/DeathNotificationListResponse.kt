package com.alphaestatevault.model

data class DeathNotificationListResponse(
    val message: String ="",
    val notifications: MutableList<Notification> = mutableListOf(),
    val success: Int =0,
    val total_count: String =""
)
{
    data class Notification(
        val notification_id: String ="",
        val user_id: String ="",
        val holder_id: String ="",
        val name: String ="",
        val holder_name: String ="",
        val phone: String ="",
        var email : String ="",
        val address: String ="",
        val holder: String ="",
        val timestamp: String =""
    )
}
