package com.devcorerd.pos.helper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat



/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
class DateHelper private constructor() {
    companion object {
        var dtf = DateTimeFormat.forPattern("dd/MM/yyyy")

        fun getDateAsString(dateTime: DateTime): String{
            return dtf.print(dateTime)
        }

        fun getDateHourAsString(dateTime: DateTime): String{
            val dtf = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a")
            return dtf.print(dateTime)
        }

        fun getStringAsDate(dateAsString: String): DateTime{
            return try{
                dtf.parseDateTime(dateAsString)
            }catch (ex: Exception){
                DateTime.now()
            }
        }
    }
}