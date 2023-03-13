package com.application.alphacapital.superapp.finplan.utils

class AppConstant
{
    companion object {
        //const val MAIN_URL = "http://current.coronation.in/online_financial_planning/api/index.php/services/"
        const val MAIN_URL = "https://php1.coronation.in/online-financial-planning/api/index.php/services/"

        const val API_ID : String = "YzMxYjMyMzY0Y2UxOWNhOGZjZDE1MGE0MTdlY2NlNTg="
        const val FROM_APP : String = "true"

        const val FINAL_REPORT:String = MAIN_URL +"generateReport/"
    }

    object camefrom{
        const val CAME_FROM: String = "CameFrom"
        const val SELECT_TIMEHORIZON: String = "Time Horizon"
        const val SELECT_RISKPROFILE: String = "Risk Profile"
        const val SELECT_TAXSLAB: String = "Tax Slab"

        const val SELECT_INVESTMENT_TYPE = "Investment Types"
        const val SELECT_LIABILITIES_TYPE = "Liabilities Types"
        const val SELECT_START_YEAR = "Start Year"
        const val SELECT_END_YEAR = "End Year"
        const val SELECT_ASPIRATION_TYPE = "Aspiration type"
        const val SELECT_PERIODICITY = "Periodicity"
        const val SELECT_LIFE_EXPECTANCY = "Life Expectancy"

        const val GRAPH_EXISTING_ASSET_ALLOCATION_MICRO = "Existing Asset Allocation(Micro)"
        const val GRAPH_EXISTING_ASSET_ALLOCATION_MACRO = "Existing Asset Allocation(Macro)"

        const val GRAPH_VARIANCE_MICRO_STRATEGIC = "Variance Analysis Micro(Strategic)"
        const val GRAPH_VARIANCE_MICRO_TACTICAL = "Variance Analysis Micro(Tactical)"
        const val GRAPH_VARIANCE_MACRO_STRATEGIC = "Variance Analysis Macro(Strategic)"
        const val GRAPH_VARIANCE_MACRO_TACTICAL = "Variance Analysis Macro(Tactical)"
        const val GRAPH_RECOMMENDED_ASSET_ALLOCATION = "Recommended Asset Allocation"
        const val GRAPH_RANGE_RETURN_RISK = "Range of Return Risk"
        const val GRAPH_BALANCES_SHEET = "Balances sheet Movement"
        const val GRAPH_WEALTH_REQUIRED = "Wealth Required"
        const val GRAPH_RISK_PROFILE_ALLOCATION = "Risk Profile Allocation"

        const val GRAPH_NEED_GAP_CURRENT = "Need Gap-Current"
        const val GRAPH_NEED_GAP_FUTURE = "Need Gap-Future"

    }

    object session{
        val KEY_IS_LOGIN: String = "loginStatus"
        val KEY_ID: String = "UserId"
        val KEY_USERNAME: String = "UserName"
        val KEY_FIRSTNAME : String = "FirstName"
        val KEY_LASTNAME : String = "FirstName"
        val KEY_EMAIL: String = "Email"
        val KEY_PHONE: String = "MobileNumber"
        val KEY_IMAGE: String = "ProfileImage"
        val KEY_FCM_TOKEN: String = "FCMToken"
        val KEY_DEVICE_ID: String = "DeviceId"
        val KEY_SOCIAL_LOGIN: String = "socialloginStatus"
        val KEY_RISK_PROFILE: String = "RiskProfile"

        val KEY_IS_INTRO_SEEN = "isIntroSeen"

        val KEY_COUNTRY_NAME = "CountryName"
        val KEY_COUNTRY_ID = "CountryId"

        val KEY_STATE_NAME = "StateName"
        val KEY_STATE_ID = "StateId"

        val KEY_CITY_NAME = "CityName"
        val KEY_CITY_ID = "CityID"

        val KEY_ADDRESS = "Address"

    }

    object other{
        const val DONT_KNOW_RISK_PROFILE: String = "Don't know My Risk Profile"
    }
}
