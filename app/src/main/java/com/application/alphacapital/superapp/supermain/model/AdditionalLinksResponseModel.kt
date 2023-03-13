package com.application.alphacapital.superapp.supermain.model

data class AdditionalLinksResponseModel(var links: MutableList<Link> = mutableListOf(), var message: String = "", var success: Int = 0)
{
    data class Link(var link: String = "", var name: String = "")
}