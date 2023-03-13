package com.alphaestatevault.model

data class MinorChildrenAdultDependentsResponse(
    var message: String ="",
    var minor_children_adult_dependents: MinorChildrenAdultDependents,
    var success: Int =0
)
{
    data class MinorChildrenAdultDependents(
        var agreed_to_assume_responsibility: String="",
        var document_located: String="",
        var have_you: String="",
        var instructions_directions_suggestions: String="",
        var mcad_id: String="",
        var minor_children_and_dependents: String="",
        var name_address_phone: String="",
        var timestamp: String="",
        var user_id: String=""
    )
}

