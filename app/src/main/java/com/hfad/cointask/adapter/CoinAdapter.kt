package com.hfad.cointask.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.hfad.cointask.R
import com.hfad.cointask.helper.AppDatabase
import com.hfad.cointask.helper.CallableNews
import com.hfad.cointask.helper.NewsDao
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.function.Consumer


@Suppress("DEPRECATION")
class CoinAdapter(var values: List<News>, var context: Context): androidx.recyclerview.widget.RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    val intent = Intent(context, NewsViewActivity::class.java)


    var db:AppDatabase = AppDatabase.getInstance(context) as AppDatabase

    object NewsFields {
        var title = ""
        var id = ""
        var newsThumb = ""
        var label: String? = ""
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(p0: CoinViewHolder, p1: Int) {



        val item = values[p1]

        isNewsInDb(item, p0)

        NewsFields.title = item.getTitle()
        p0.newsTitle.text = NewsFields.title

        NewsFields.id = item.getId().toString()
        val idItem = NewsFields.id

        NewsFields.label = item.getBadge()?.getLabel()
        val labelItem = NewsFields.label

        NewsFields.newsThumb = item.getThumb()
        val thumbItem = NewsFields.newsThumb



        if (labelItem == "default") {

            p0.newsThumb.setImageDrawable(null)

            } else {
            p0.newsThumb.visibility = View.VISIBLE
            Picasso.get()
                    .load(thumbItem)
                    .into(p0.newsThumb)
        }

        p0.setItemClickListener(object: ItemClickListener {
            override fun onClick(v: View, position: Int, isLongClick: Boolean) {

                intent.putExtra("id", idItem)
                context.startActivity(intent)

            }

        })

        setOnImageClickListener(p0.saveNews, item)


    }

    override fun getItemCount(): Int {

        return values.size

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
    private fun isNewsInDb(item: News, p0: CoinViewHolder) {

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


    private fun setOnImageClickListener(img: ImageView, item: News) {

        img.setOnClickListener {
            if (img.tag == "saved") {
                img.setImageResource(R.drawable.save_icon_outline_24)
                img.tag = "not saved"
                deleteFromDb(item, db)
            } else {
                img.setImageResource(R.drawable.save_icon_24)
                img.tag = "saved"
                saveToDb(item, db)
            }

        }

        img.setOnLongClickListener {
            img.setImageResource(R.drawable.save_icon_24)
             true
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CoinViewHolder {

        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.news_item_view, p0, false)
        return CoinViewHolder(view)

    }


    class CoinViewHolder(itemView: View): ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {

        var newsTitle: TextView
        var newsThumb: ImageView
        var saveNews: ImageView

        private lateinit var itemClickListener:ItemClickListener
        lateinit var transition: TransitionDrawable

        fun setItemClickListener(itemClickListener: ItemClickListener) {

            this.itemClickListener = itemClickListener

        }

        init {
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
            newsTitle = itemView.findViewById(R.id.item_title)
            newsThumb = itemView.findViewById(R.id.thumb_image)
            saveNews = itemView.findViewById(R.id.save_icon)
        }


        override fun onClick(v: View) {

            itemClickListener.onClick(v, adapterPosition, false)
            v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.tap))

        }

        override fun onLongClick(v: View): Boolean {

            //itemClickListener.onClick(v, adapterPosition, true)
            //v.startAnimation(AnimationUtils.loadAnimation(v.context, R.anim.tap))
            return true

        }


    }
}