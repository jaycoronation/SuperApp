package com.alphaestatevault.model

data class WillDetailResponse(
    var message: String ="",
    var success: Int =0,
    var will: Will
)
{
    data class Will(
        var original_will_located: String ="",
        var upload_doc_review : String = "",
        var timestamp: String ="",
        var user_id: String ="",
        var will_id: String =""
    )
}

