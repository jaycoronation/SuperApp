package com.alphaestatevault.model

data class AddCharityRelatedObligationsGetSet(
    var holder:String = "",
    var organization: String="",
    var nature_of_obligation: String="",
    var instructions: String="",
    var contact : String ="",
    var phone : String="",
    var address : String ="",
    var charity_related_obligation_id:String = "")