package com.alphafinancialplanning.model

data class UserProfileResponse(
    val message: String,
    val profile: Profile,
    val success: Int
) {
    data class Profile(
        val age: String,
        val amount_invested: String,
        val dob: String,
        val email: String,
        val first_name: String,
        val is_active: String,
        val last_name: String,
        val life_expectancy: String,
        val mobile: String,
        val retirement_age: String,
        val risk_profile: String,
        val tax_slab: String,
        val time_horizon: String,
        val timestamp: String,
        val user_id: String
    )
}