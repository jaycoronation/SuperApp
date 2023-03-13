package com.alphaestatevault.model

data class CountriesResponse(
    val countries: MutableList<Country> = mutableListOf(),
    val message: String ="",
    val success: Int =0
)
{
    data class Country(
        val id: String ="",
        val name: String =""
    )
}
