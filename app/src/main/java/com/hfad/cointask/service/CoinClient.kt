package com.hfad.cointask.service

import com.hfad.cointask.model.DataLIst
import com.hfad.cointask.model.NewsList
import retrofit2.Call
import retrofit2.http.*

interface CoinClient {
    @GET("api/v1/mobile/feed")
    fun getFeed(): Call<String>
}