package com.hfad.cointask.model

import java.util.*

class Published(private var date: Date, private var timezone_type: Int, private var timezone: String) {

    fun getDate(): Date {
        return date
    }

    fun getTimeType(): Int {
        return timezone_type
    }

    fun getTimezone(): String {
        return timezone
    }

}