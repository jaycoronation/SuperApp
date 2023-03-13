package com.application.alphacapital.superapp.supermain.model

data class FilterResponseModel(
    val url: MutableList<Url>
) {
    data class Url(
        val title: String, // All
        val url: String, // https://www.alphacapital.in/blog/feed/json
        var isSelected: Boolean = false
    )
}