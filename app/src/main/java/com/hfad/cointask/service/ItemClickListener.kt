package com.hfad.cointask.service

import android.view.View
import com.hfad.cointask.model.News

interface ItemClickListener {

    fun onClick(v:View, position: Int, isLongClick: Boolean)

}