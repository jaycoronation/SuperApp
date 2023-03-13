package com.alphaestatevault.model
import android.graphics.drawable.Drawable

data class MenuGetSetForHome
(
        var menuName:String = "",
        var isOpen : Boolean =false,
        var menulist: MutableList<MenuGetSet> = mutableListOf()
)
{
    data class MenuGetSet(
            var menuId: String = "",
            var menuName:String = "",
            var drawable: Drawable? = null
    )
}

