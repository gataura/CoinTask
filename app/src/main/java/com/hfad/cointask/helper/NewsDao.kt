package com.hfad.cointask.helper

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import com.hfad.cointask.model.News

@Dao
interface NewsDao {

    @Insert
    fun insert(news: News)

    @Update
    fun update(news:News)

    @Delete
    fun delete(news: News)

}