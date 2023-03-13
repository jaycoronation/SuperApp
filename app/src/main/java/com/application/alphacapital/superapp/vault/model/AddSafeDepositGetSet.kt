package com.alphaestatevault.model

data class AddSafeDepositGetSet(
    var location: String ="",
    var box_number: String="",
    var person_authorized_to_sign: String="",
    var person_with_keys:String = "",
    var notes:String = "",
    var holder: String="",
    var safe_deposit_box_id:String = "")