package com.hfad.cointask.helper

import com.hfad.cointask.FeedActivity
import com.hfad.cointask.model.News
import io.reactivex.Flowable
import java.util.concurrent.Callable

open class CallableNews(data: News): Callable<News> {

    private var data: News = data

    override fun call(): News {
        return data
    }
}