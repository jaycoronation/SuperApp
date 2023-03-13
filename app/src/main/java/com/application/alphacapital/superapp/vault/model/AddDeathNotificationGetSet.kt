package com.alphaestatevault.model

import android.graphics.drawable.Drawable

class AddDeathNotificationGetSet
{
    var name: String =""
    var phone: String=""
    var email: String=""
    var address: String=""
    var holder: String=""
    var notification_id:String = ""

    constructor(name: String, phone: String,email : String, address: String,holder : String,notification_id:String)
    {
        this.name = name
        this.phone = phone
        this.email = email
        this.address = address
        this.holder = holder
        this.notification_id = notification_id
    }
}