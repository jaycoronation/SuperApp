package com.application.alphacapital.superapp.acpital.model

data class MenuTabResponseModel(
    val BannerImage: String,
    val Discription: String,
    val MainTab: ArrayList<MainTabnew>,
    val Title: String,
    val status: String
) {
    data class MainTabnew(
        val IsSubMenu: Boolean,
        val MenuDescription: String,
        val MenuIcon: String,
        val MenuId: Int,
        val MenuTitle: String
    )
}