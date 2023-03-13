package com.alphaestatevault.model

data class AddAssetsNotPossessionGetSet(
    var holder:String = "",
    var title:String ="",
    var description: String ="",
    var approximate_value: String="",
    var documentation: String="",
    var location_of_documentation: String="",
    var notes: String="",
    var possession_id:String = "")