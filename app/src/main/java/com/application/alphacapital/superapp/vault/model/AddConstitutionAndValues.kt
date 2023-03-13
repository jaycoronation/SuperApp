package com.alphaestatevault.model


class AddConstitutionAndValues
{
    var notes: String =""
    var constitution_and_values_id:String = ""

    constructor(notes: String,constitution_and_values_id:String)
    {
        this.notes = notes
        this.constitution_and_values_id = constitution_and_values_id
    }
}