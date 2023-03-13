package com.alphaestatevault.model

data class DomesticEmployeesDetailResponse(
    var domestic_employees: DomesticEmployees,
    var message: String ="",
    var success: Int =0
)

{
    data class DomesticEmployees(
        var domestic_employees_id: String="",
        var employee_instruction: String="",
        var is_domestic_employee: String="",
        var timestamp: String="",
        var user_id: String=""
    )
}

