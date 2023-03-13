package com.alphaestatevault.model

data class AddInsurancePolicyGetSet(
    var insuranceCompany: String ="",
    var typeofpolicy: String="",
    var policynumber: String="",
    var personthinginsured: String="",
    var sumassured: String="",
    var currentvalue: String="",
    var purchasedon: String="",
    var agentname: String="",
    var agentphone: String="",
    var agentaddress: String="",
    var locationofdocument: String="",
    var uploaddocument: String="",
    var notes : String ="",
    var holder:String = "",
    var insurancepolicyId:String = "",
    var nominee_name : String ="")