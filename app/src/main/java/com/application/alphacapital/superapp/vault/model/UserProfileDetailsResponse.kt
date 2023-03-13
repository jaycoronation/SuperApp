package com.alphaestatevault.model

data class UserProfileDetailsResponse(
    val message: String,
    val profile: Profile,
    val success: Int
)

data class Profile(
    val city: String,
    val city_name: String,
    val country: String,
    val country_name: String,
    val email: String,
    val mobile: String,
    val name: String,
    val profile_pic: String,
    val profile_pic_name: String,
    val state: String,
    val state_name: String,
    val timestamp: String,
    val user_id: String
)