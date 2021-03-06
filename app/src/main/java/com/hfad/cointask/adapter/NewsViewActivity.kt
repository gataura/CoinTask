package com.hfad.cointask.adapter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import com.hfad.cointask.R

@Suppress("DEPRECATION")
class NewsViewActivity : AppCompatActivity() {

    lateinit var id:String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)


        var webView = findViewById<WebView>(R.id.news_view)
        id = intent.getStringExtra("id")
        webView.loadUrl("https://api.cointelegraph.com/api/v1/mobile/newsById/$id")
        webView.settings.javaScriptEnabled = true

        intent.removeExtra("id")

    }

}
