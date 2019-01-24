package com.hfad.cointask.service

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*

interface CoinClient {
    @GET("api/v1/mobile/feed")
    fun getHead(): Call<String>

    @GET("api/v1/mobile/latest?")
    fun getNews(@Query("offset") offset:Int, @Query("length") length:Int): Flowable<String>
}