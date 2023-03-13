package com.alphaestatevault.model

import android.graphics.drawable.Drawable

class MenuGetSetForNavigation
{
    var menuId = ""
    var menuName:String = ""
    var drawable: Drawable? = null

    constructor(menuId: String, menuName: String, drawable: Drawable?)
    {
        this.menuId = menuId
        this.menuName = menuName
        this.drawable = drawable
    }
}