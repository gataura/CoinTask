package com.hfad.cointask.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.cointask.R
import com.hfad.cointask.helper.AppDatabase
import com.hfad.cointask.helper.CoinViewHolder
import com.hfad.cointask.helper.NewsItem
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class SavedAdapter(var values: List<News>, var context: Context): RecyclerView.Adapter<CoinViewHolder>() {

    var helper = NewsItem(context)
    private var db: AppDatabase = AppDatabase.getInstance(context) as AppDatabase
    val intent = Intent(context, NewsViewActivity::class.java)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        val item = values[position]

        holder.saveNews.tag = "saved"
        holder.saveNews.setImageResource(R.drawable.save_icon_24)

        helper.setNewsItem(holder, item,context)


        setOnImageClickListener(holder.saveNews, item)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.news_item_view, parent, false)
        return CoinViewHolder(view)

    }


}