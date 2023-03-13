package com.alphaestatevault.model

data class AddFinancialInstitutionAccountGetSet(
    var bankName : String ="",
    var branch: String="",
    var accountNumberAndType: String="",
    var mode_of_holding : String ="",
    var approximateValue : String="",
    var upload_doc: String="",
    var notes : String ="",
    var holder:String = "",
    var financialInstitutionAccountId:String = "",
    var nominee_name : String ="")