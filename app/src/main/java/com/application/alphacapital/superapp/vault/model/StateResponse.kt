package com.alphaestatevault.model

data class StateResponse(
    val states: MutableList<State> = mutableListOf(),
    val success: Int =0
)
{
    data class State(
            val country_id: String ="",
            val id: String="",
            val name: String=""
    )
}
