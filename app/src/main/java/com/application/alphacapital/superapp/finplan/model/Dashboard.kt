package com.application.alphacapital.superapp.finplan.model

data class Dashboard(
    val id:String = "",
    val icon:Int = 0,
    val icon_white:Int = 0,
    val title:String = "",
    var menulist: MutableList<MenuGetSet> = mutableListOf(),
    var isOpen:Boolean = false,
    ){
    data class MenuGetSet(
        var menuId: String = "",
        var menuName:String = "",
        var drawable: Int = 0
    )
}