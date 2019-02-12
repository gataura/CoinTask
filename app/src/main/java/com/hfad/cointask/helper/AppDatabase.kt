package com.hfad.cointask.helper

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.hfad.cointask.model.News

@Database(entities = [News::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}