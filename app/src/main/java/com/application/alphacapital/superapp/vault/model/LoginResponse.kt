package com.alphaestatevault.model

data class LoginResponse(
    val city: String ="",
    val city_name: String="",
    val country: String="",
    val country_name: String="",
    val email: String="",
    val message: String="",
    val mobile: String="",
    val name: String="",
    val profile_pic: String="",
    val state: String="",
    val state_name: String="",
    val success: Int =0,
    val timestamp: String ="",
    val user_id: String=""
)