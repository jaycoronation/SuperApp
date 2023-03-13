package com.alphaestatevault.model

data class AddRealPropertyGetSet(
    var type_of_property: String="",
    var name: String="",
    var location: String="",
    var encumbrances : String="",
    var approximate_value : String ="",
    var loan: String="",
    var rent: String="",
    var purchased_on: String="",
    var location_of_document : String="",
    var upload_doc : String ="",
    var notes : String ="",
    var holder:String = "",
    var real_property_id:String = "",
    var nominee_name : String ="")