package com.devcorerd.pos.helper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*


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

        fun getStringHourAsDate(dateAsString: String): DateTime{
            return try{
                val dtf = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a")
                dtf.parseDateTime(dateAsString)
            }catch (ex: Exception){
                DateTime.now()
            }
        }

        fun getDateAsLongString(date: DateTime): String {
            val locale = Locale("ES")

            val day: Int = date.dayOfMonth().getAsText(locale).toInt()
            val dayString: String

            dayString = if (day < 10)
                "0".plus(day)
            else
                day.toString()

            return date.dayOfWeek().getAsText(locale).capitalize().plus(" ")
                    .plus(dayString).plus(" de ")
                    .plus(date.monthOfYear().getAsText(locale).capitalize())
        }

        fun getHourString(date: DateTime): String {
            val locale = Locale("ES")

            val hour: Int = date.hourOfDay().getAsText(locale).toInt()
            val minute: Int = date.minuteOfHour().getAsText(locale).toInt()

            return getHourAsString(hour, minute).trim()
        }

        private fun getHourAsString(hour: Int, minute: Int): String {
            var hourString: String
            val minuteString: String = if (minute < 10)
                "0".plus(minute)
            else
                minute.toString()
            val meridian: String

            when {
                hour == 0 -> {
                    hourString = "12"
                    meridian = "am"
                }
                hour < 10 -> {
                    hourString = "0".plus(hour.toString())
                    meridian = "am"
                }
                hour > 12 -> {
                    hourString = (hour - 12).toString()
                    if ((hour - 12) < 10) {
                        hourString = "0".plus(hourString)
                    }
                    meridian = "pm"
                }
                else -> {
                    hourString = hour.toString()
                    meridian = "am                  "
                }
            }

            return hourString.plus(":").plus(minuteString).plus(" ").plus(meridian)

        }
    }
}