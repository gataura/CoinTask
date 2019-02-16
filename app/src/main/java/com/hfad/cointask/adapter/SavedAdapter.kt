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
import com.hfad.cointask.helper.NewsItem
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class SavedAdapter(var values: List<News>, var context: Context): RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    var helper = NewsItem()
    private var db: AppDatabase = AppDatabase.getInstance(context) as AppDatabase
    val intent = Intent(context, NewsViewActivity::class.java)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {

        val item = values[position]

        holder.saveNews.tag = "saved"
        holder.saveNews.setImageResource(R.drawable.save_icon_24)

        NewsItem.NewsFields.title = item.getTitle()
        holder.newsTitle.text = NewsItem.NewsFields.title

        NewsItem.NewsFields.id = item.getId().toString()
        val idItem = NewsItem.NewsFields.id

        NewsItem.NewsFields.label = item.getBadge()?.getLabel()
        val labelItem = NewsItem.NewsFields.label

        NewsItem.NewsFields.newsThumb = item.getThumb()
        val thumbItem = NewsItem.NewsFields.newsThumb



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

                intent.putExtra("id", idItem)
                context.startActivity(intent)

            }

        })

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.news_item_view, parent, false)
        return SavedViewHolder(view)

    }

    class SavedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

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