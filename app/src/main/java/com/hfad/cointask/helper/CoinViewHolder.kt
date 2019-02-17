package com.hfad.cointask.helper

import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.cointask.R
import com.hfad.cointask.service.ItemClickListener

class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {

    var newsTitle: TextView
    var newsThumb: ImageView
    var saveNews: ImageView
    var badgeTitle: TextView
    var timeAndViews: TextView
    var newsCard: CardView

    private lateinit var itemClickListener: ItemClickListener
    lateinit var transition: TransitionDrawable

    fun setItemClickListener(itemClickListener: ItemClickListener) {

        this.itemClickListener = itemClickListener

    }

    init {
        newsTitle = itemView.findViewById(R.id.item_title)
        newsThumb = itemView.findViewById(R.id.thumb_image)
        saveNews = itemView.findViewById(R.id.save_icon)
        badgeTitle = itemView.findViewById(R.id.badge_title)
        timeAndViews = itemView.findViewById(R.id.time_views)
        newsCard = itemView.findViewById(R.id.news_card)
        newsCard.setOnLongClickListener(this)
        newsCard.setOnClickListener(this)
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