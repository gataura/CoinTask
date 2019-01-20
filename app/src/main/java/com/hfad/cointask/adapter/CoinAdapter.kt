package com.hfad.cointask.adapter

import android.support.v7.view.menu.MenuView
import android.support.v7.view.menu.MenuView.ItemView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hfad.cointask.R
import com.hfad.cointask.model.News

class CoinAdapter(var values: List<News>): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {


    override fun onBindViewHolder(p0: CoinViewHolder, p1: Int) {

        val itemTitle: News = values[p1]
        p0.newsTitle.text = itemTitle.getTitle()

    }

    override fun getItemCount(): Int {

        return values.size

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CoinViewHolder {

        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.news_item_view, p0, false)
        return CoinViewHolder(view)

    }

    class CoinViewHolder(itemView: View): ViewHolder(itemView) {

        var newsTitle: TextView = itemView.findViewById(R.id.item_title)

    }
}