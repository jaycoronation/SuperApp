package com.alphaestatevault.model

data class GenerallyDetailsResponse(
    var generally: Generally,
    var message: String ="",
    var success: Int =0
) {
    data class Generally(
        var directions_about_medical_and_nursing: String = "",
        var generally_id: String = "",
        var instructions_about_funeral: String = "",
        var organ_donor_transplantation_wishes: String = "",
        var timestamp: String = "",
        var user_id: String = ""
    )
}