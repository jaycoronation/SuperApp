package com.alphaestatevault.model

data class AddInvestmentTrustAccountGetSet(
    var asset_name :String = "",
    var institution :String = "",
    var account_number :String = "",
    var amount :String = "",
    var contact_person :String = "",
    var location_of_document :String = "",
    var upload_doc : String ="",
    var notes :String = "",
    var holder:String = "",
    var investment_trust_account_id:String = "",
    var nominee_Name:String = "")