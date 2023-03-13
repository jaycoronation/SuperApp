package com.alphaestatevault.model

import android.graphics.drawable.Drawable

class AddBusinessDetailsGetSet
{
    var name: String = ""
    var lineorArea: String = ""
    var type: String = ""
    var holder: String = ""
    var holder_id: String = ""
    var holder_userName: String = ""

    constructor(name: String, lineorArea: String, type: String, holder: String, holder_id: String, holder_userName: String)
    {
        this.name = name
        this.lineorArea = lineorArea
        this.type = type
        this.holder = holder
        this.holder_id = holder_id
        this.holder_userName = holder_userName
    }

    constructor()


}