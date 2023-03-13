package com.application.alphacapital.superapp.supermain.model

data class FeedResponseModel(
    var description: String = "",
    var feed_url: String = "",
    var home_page_url: String = "",
    var icon: String = "",
    var items: MutableList<Item> = mutableListOf(),
    var language: String = "",
    var next_url: String = "",
    var title: String = "",
    var user_comment: String = "",
    var version: String = ""
)