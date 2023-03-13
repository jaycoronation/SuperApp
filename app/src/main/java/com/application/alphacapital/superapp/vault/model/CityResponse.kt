package com.alphaestatevault.model

data class CityResponse(
    val cities: MutableList<City> = mutableListOf(),
    val success: Int =0
)
{
    data class City(
            val id: String ="",
            val name: String ="",
            val state_id: String=""
    )
}
