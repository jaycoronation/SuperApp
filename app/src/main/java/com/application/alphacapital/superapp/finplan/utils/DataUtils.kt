package com.application.alphacapital.superapp.finplan.utils

import com.application.alphacapital.superapp.finplan.utils.AppConstant.other.DONT_KNOW_RISK_PROFILE
import java.util.*

class DataUtils
{
    companion object{
        fun getTimeHorizon():MutableList<String>{
            val list:MutableList<String> = mutableListOf()
            list.add("0-6 months")
            list.add("6 months to 1 year")
            list.add("2 years to 3 years")
            list.add("4 years to 10 years")
            list.add("10 years & more")
            return list
        }

        fun getRiskProfile():MutableList<String>{
            val list:MutableList<String> = mutableListOf()
            list.add("Moderate")
            list.add("Conservative")
            list.add("Aggressive")
            list.add(DONT_KNOW_RISK_PROFILE)
            return list
        }

        fun getTaxSlab():MutableList<String>{
            val list:MutableList<String> = mutableListOf()
            list.add("10%")
            list.add("20%")
            list.add("30%")
            return list
        }

        fun getYear():MutableList<String>{
            val list:MutableList<String> = mutableListOf()

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            for (i in currentYear..currentYear+100){
                list.add("$i")
            }
            return list
        }

        fun getPeriodicity():MutableList<String>{
            val list:MutableList<String> = mutableListOf()
            for (i in 1..20){
                list.add("$i")
            }
            return list
        }
        fun getLifeExpectancy():MutableList<String>{
            val list:MutableList<String> = mutableListOf()
            list.add("Below 50")
            list.add("50 to 60")
            list.add("60 to 70")
            list.add("70 to 80")
            list.add("80 to 90")
            list.add("90 to 100")
            list.add("Above 100")
            return list
        }
    }
}