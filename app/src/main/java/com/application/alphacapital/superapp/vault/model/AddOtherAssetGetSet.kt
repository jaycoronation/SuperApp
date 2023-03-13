package com.alphaestatevault.model

data class AddOtherAssetGetSet(
    var holder:String = "",
    var description : String ="",
    var encumbrances: String="",
    var approximateValue : String="",
    var notes: String="",
    var other_asset_id:String = "",
    var nominee_name : String ="")