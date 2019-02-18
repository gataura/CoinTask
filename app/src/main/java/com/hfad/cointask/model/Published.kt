package com.hfad.cointask.model

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Published(private var date: Date, private var timezoneType: Int, private var timezone: String) {

    fun getDate(): Date {
        return date
    }

    fun getTimeType(): Int {
        return timezoneType
    }

    fun getTimezone(): String {
        return timezone
    }


    @SuppressLint("SimpleDateFormat")
    override fun toString(): String {

        var df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")


        return df.format(date) + ";" + timezoneType.toString() + ";" + timezone
    }

}