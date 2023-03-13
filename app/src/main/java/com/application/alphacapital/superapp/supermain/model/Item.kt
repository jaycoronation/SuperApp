package com.application.alphacapital.superapp.supermain.model

data class Item(
    val author: Author,
    val authors: List<Author>,
    val content_html: String,
    val content_text: String,
    val date_modified: String,
    val date_published: String,
    val id: String,
    val image: String,
    val tags: List<String>,
    val title: String,
    val url: String
)