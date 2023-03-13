package com.application.alphacapital.superapp.supermain.model

data class SuperLoginResponseModel(
    val message: String = "",
    val portfolio: Portfolio = Portfolio(),
    val success: Int = 0,
    val vault: Vault = Vault(),
    val profile: Profile = Profile()
) {
    data class Portfolio(
        val email: String = "",
        val first_name: String = "",
        val last_name: String = "",
        val pan_no: String = "",
        val user_id: String = "",
        val dob: String = ""
    )

    data class Vault(
        val city_id: String = "",
        val city_name: String = "",
        val country_id: String = "",
        val country_name: String = "",
        val email: String = "",
        val image: String = "",
        val phone: String = "",
        val state_id: String = "",
        val state_name: String = "",
        val user_id: String = "",
        val dob: String = "",
        val username: String = ""
    )

    data class Profile(
        val city_id: String = "",
        val city_name: String = "",
        val country_id: String = "",
        val country_name: String = "",
        val email: String = "",
        val image: String = "",
        val phone: String = "",
        val state_id: String = "",
        val state_name: String = "",
        val dob: String = "",
        val user_id: String = "",
        val username: String = ""
    )
}