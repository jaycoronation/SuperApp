package com.alphaestatevault.model

data class AddFiduciaryObligationsResponse(
    var holder: String="",
    var name: String="",
    var relationship: String="",
    var types_of_records: String="",
    var instructions : String = "",
    var contact: String="",
    var phone: String="",
    var address: String="",
    var created_on : String ="",
    var notes : String ="",
    var fiduciary_obligation_id: String=""
)

