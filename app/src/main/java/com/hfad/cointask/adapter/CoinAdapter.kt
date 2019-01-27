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
import com.hfad.cointask.model.News
import com.hfad.cointask.service.ItemClickListener
import com.squareup.picasso.Picasso


@Suppress("DEPRECATION")
class CoinAdapter(var values: List<News>, var context: Context): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    val intent = Intent(context, NewsViewActivity::class.java)

    override fun onBindViewHolder(p0: CoinViewHolder, p1: Int) {


        val item: News = values[p1]

        p0.newsTitle.text = item.getTitle()


        var idItem = item.getId()


        var labelItem = item.getBadge()?.getLabel()



        if (labelItem == "default") {

            p0.newsThumb.setImageDrawable(null)

            } else {
            p0.newsThumb.visibility = View.VISIBLE
            Picasso.get()
                    .load(item.getThumb())
                    .into(p0.newsThumb)
        }

        p0.setItemClickListener(object: ItemClickListener {
            override fun onClick(v: View, position: Int, isLongClick: Boolean) {

                intent.putExtra("id", idItem.toString())
                context.startActivity(intent)

            }

        })

    }

    override fun getItemCount(): Int {

        return values.size

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

    }
}