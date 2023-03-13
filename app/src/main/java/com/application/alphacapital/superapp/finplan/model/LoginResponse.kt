package com.application.alphacapital.superapp.finplan.model

data class LoginResponse(
    val age: String,
    val amount_invested: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val life_expectancy: String,
    val message: String,
    val mobile: String,
    val retirement_age: String,
    val risk_profile: String,
    val success: Int,
    val tax_slab: String,
    val time_horizon: String,
    val timestamp: String,
    val user_id: String
)