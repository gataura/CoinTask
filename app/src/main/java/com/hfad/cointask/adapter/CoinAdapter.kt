package com.hfad.cointask.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.hfad.cointask.R
import com.hfad.cointask.helper.AppDatabase
import com.hfad.cointask.helper.NewsDao
import com.hfad.cointask.model.Badge
import com.hfad.cointask.model.News
import com.hfad.cointask.model.Published
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso
import java.util.*


@Suppress("DEPRECATION")
class CoinAdapter(var values: List<News>, var context: Context): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    val intent = Intent(context, NewsViewActivity::class.java)

   // var db: AppDatabase = App.getInstance().getDatabase()
   // var newsDao: NewsDao = db.newsDao()

    object NewsFields {
        var title = ""
        var id = ""
        var newsThumb = ""
        var label: String? = ""
    }

    override fun onBindViewHolder(p0: CoinViewHolder, p1: Int) {


        val item: News = values[p1]

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

        setOnImageClickListener(p0.saveNews)


    }

    override fun getItemCount(): Int {

        return values.size

    }

    fun saveToDb() {

        var news = News(
                id = NewsFields.id.toInt(),
                title = NewsFields.title,
                thumb = NewsFields.newsThumb,
                badge = Badge(
                        label = NewsFields.label.toString(),
                        title = ""
                ),
                audio = "",
                author = "",
                author_id = 0,
                published_at = Published(
                        date = Date(),
                        timezone = "",
                        timezone_type = 0
                ),
                share_url = "",
                isSponsored = false,
                views = 0,
                lead = "",
                tag = "",
                type = ""
        )

        //newsDao.insert(news)
    }

    fun deleteFromDb() {

    }

    private fun setOnImageClickListener(img: ImageView) {

        img.setOnClickListener {
            if (img.tag == "saved") {
                img.setImageResource(R.drawable.save_icon_outline_24)
                img.tag = "not saved"
            } else {
                img.setImageResource(R.drawable.save_icon_24)
                img.tag = "saved"
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

        private lateinit var itemClickListener:ItemClickListener
        lateinit var transition: TransitionDrawable

        fun setItemClickListener(itemClickListener: ItemClickListener) {

            this.itemClickListener = itemClickListener

        }

        init {
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
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

        var newsTitle: TextView = itemView.findViewById(R.id.item_title)
        var newsThumb: ImageView = itemView.findViewById(R.id.thumb_image)
        var saveNews: ImageView = itemView.findViewById(R.id.save_icon)

    }
}