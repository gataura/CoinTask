package com.hfad.cointask.helper

import androidx.room.TypeConverter
import com.hfad.cointask.model.Badge
import com.hfad.cointask.model.Published
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NewsConverter {

    @TypeConverter
    fun fromPublished(publ: Published?): String {
        return publ.toString()
    }

    @TypeConverter
    fun toPublished(str: String): Published {
        var array = str.split(";")
        var format: DateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS")
        var date = format.parse(array[0])
        var publ = Published(date, array[1].toInt(), array[2])

        return publ
    }

    @TypeConverter
    fun fromBadge(badge: Badge): String {
        return badge.toString()
    }

    @TypeConverter
    fun toBadge(str:String): Badge {
        var array = str.split(";")
        return Badge(array[0], array[1])
    }

}