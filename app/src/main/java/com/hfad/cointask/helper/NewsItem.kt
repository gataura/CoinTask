package com.hfad.cointask.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.hfad.cointask.R
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.adapter.NewsViewActivity
import com.hfad.cointask.adapter.SavedAdapter
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import com.hfad.cointask.helper.NewsItem
import org.ocpsoft.prettytime.PrettyTime
import java.text.DateFormat
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class NewsItem(context: Context) {


    object NewsFields {
        var title = ""
        var id = ""
        var newsThumb = ""
        var label: String? = ""
        var badgeTitle: String? = ""
        var time: Date? = null
        var views = 0
    }

    @SuppressLint("SimpleDateFormat")
    var df: DateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS")
    var timePubl: Long = 0
    var prettyTime = PrettyTime()
    var timeViews = ""
    val intent = Intent(context, NewsViewActivity::class.java)

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
    fun isNewsInDb(item: News, p0: CoinViewHolder, db: AppDatabase) {

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

    fun setNewsItem(holder: CoinViewHolder, item: News, context:Context) {

        NewsFields.title = item.getTitle()
        holder.newsTitle.text = NewsFields.title

        NewsFields.label = item.getBadge()?.getLabel()
        val labelItem = NewsFields.label

        NewsFields.newsThumb = item.getThumb()
        val thumbItem = NewsFields.newsThumb

        NewsFields.badgeTitle = item.getBadge()?.getTitle()
        holder.badgeTitle.text = NewsFields.badgeTitle

        NewsFields.time = item.getPublished_at()?.getDate()
        NewsFields.views = item.getViews()

        timeViews = prettyTime.format(NewsFields.time) + " • " + NewsFields.views.toString() + " views"

        holder.timeAndViews.text = timeViews

        if (labelItem == "default") {

            holder.newsThumb.setImageDrawable(null)

        } else {
            holder.newsThumb.visibility = View.VISIBLE
            Picasso.get()
                    .load(thumbItem)
                    .into(holder.newsThumb)
        }

        holder.setItemClickListener(object: ItemClickListener {
            override fun onClick(v: View, position: Int, isLongClick: Boolean) {

                intent.putExtra("id", item.getId().toString())
                context.startActivity(intent)

            }

        })


    }



}