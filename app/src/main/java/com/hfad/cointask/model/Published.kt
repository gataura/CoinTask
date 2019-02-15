package com.hfad.cointask.model

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


    override fun toString(): String {
        return date.toString() + ";" + timezoneType.toString() + ";" + timezone
    }

}