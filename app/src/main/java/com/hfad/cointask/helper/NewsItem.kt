package com.hfad.cointask.helper

import android.annotation.SuppressLint
import android.util.Log
import com.hfad.cointask.R
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.adapter.SavedAdapter
import com.hfad.cointask.model.News
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsItem {


    object NewsFields {
        var title = ""
        var id = ""
        var newsThumb = ""
        var label: String? = ""
    }

    @SuppressLint("CheckResult")
    fun saveToDb(data: News, db: AppDatabase) {

        Completable.fromAction{ db.newsDao().insert(data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    Log.d("SaveToDb", "Again", it)
                })

    }

    @SuppressLint("CheckResult")
    fun deleteFromDb(data: News, db: AppDatabase) {

        Completable.fromAction{ db.newsDao().delete(data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                    Log.d("SaveToDb", "Again", it)
                })

    }

    @SuppressLint("CheckResult")
    fun isNewsInDb(item: News, p0: CoinAdapter.CoinViewHolder, db: AppDatabase) {

        Observable.fromCallable{db.newsDao().newsCount(item.getId())}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    if (it> 0) {
                        p0.saveNews.setImageResource(R.drawable.save_icon_24)
                        p0.saveNews.tag = "saved"
                    }
                }

    }



}