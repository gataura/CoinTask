package com.hfad.cointask.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.hfad.cointask.R
import com.hfad.cointask.helper.AppDatabase
import com.hfad.cointask.helper.CoinViewHolder
import com.hfad.cointask.helper.NewsItem
import com.hfad.cointask.helper.NewsItem.NewsFields.title
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso



@Suppress("DEPRECATION")
class CoinAdapter(var values: List<News>, var context: Context, var helper: NewsItem, var db: AppDatabase): androidx.recyclerview.widget.RecyclerView.Adapter<CoinViewHolder>() {


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(p0: CoinViewHolder, p1: Int) {



        val item = values[p1]

        helper.isNewsInDb(item, p0, db)

        helper.setNewsItem(p0, item, context)

        setOnImageClickListener(p0.saveNews, item)


    }

    override fun getItemCount(): Int {

        return values.size

    }



    private fun setOnImageClickListener(img: ImageView, item: News) {

        img.setOnClickListener {
            if (img.tag == "saved") {
                img.setImageResource(R.drawable.save_icon_outline_24)
                img.tag = "not saved"
                helper.deleteFromDb(item, db)
            } else {
                img.setImageResource(R.drawable.save_icon_24)
                img.tag = "saved"
                helper.saveToDb(item, db)
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


}