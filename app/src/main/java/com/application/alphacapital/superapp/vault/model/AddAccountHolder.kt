package com.alphaestatevault.model

import android.graphics.drawable.Drawable

class AddAccountHolder
{
    var name: String =""
    var phone: String=""
    var address: String=""
    var email :String =""
    var holder: String=""
    var account_holder_id:String = ""

    constructor(name: String, phone: String, email: String, address: String,holder : String,account_holder_id:String)
    {
        this.name = name
        this.phone = phone
        this.email = email
        this.address = address
        this.holder = holder
        this.account_holder_id = account_holder_id
    }
}