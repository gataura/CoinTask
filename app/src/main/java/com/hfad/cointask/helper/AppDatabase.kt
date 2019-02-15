package com.hfad.cointask.helper

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.hfad.cointask.model.News

@Database(entities = [News::class], version = 1)
abstract class AppDatabase: RoomDatabase() {


    abstract fun newsDao(): NewsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context:Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "news.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}