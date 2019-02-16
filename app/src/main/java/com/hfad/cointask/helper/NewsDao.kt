package com.hfad.cointask.helper

import androidx.room.*
import com.hfad.cointask.model.News
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface NewsDao {

    @Insert
    fun insert(news: News)

    @Update
    fun update(news:News)

    @Delete
    fun delete(news: News)

    @Query("SELECT COUNT(*) from newsData WHERE id = :id")
    fun newsCount(id: Int): Int

    @Query("SELECT * from newsData")
    fun getAll(): List<News>

}