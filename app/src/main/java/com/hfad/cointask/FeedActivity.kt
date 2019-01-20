package com.hfad.cointask

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.model.DataLIst
import com.hfad.cointask.model.News
import com.hfad.cointask.model.NewsList
import com.hfad.cointask.service.CoinClient
import kotlinx.android.synthetic.main.activity_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FeedActivity : AppCompatActivity() {

    var news = mutableListOf<News>()
    val gson = GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS").create()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: CoinAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val client = mCoinClient().build()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        newsRecyclerView = findViewById(R.id.news_recycler_view)
        newsRecyclerView.setNestedScrollingEnabled(false)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        newsRecyclerView.layoutManager = layoutManager
        newsAdapter = CoinAdapter(news)
        newsRecyclerView.adapter = newsAdapter

        news.clear()

        headerNews()
        latestNews()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun headerNews() {
        getHeader().enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var headNews = response.body()
                jsonHeader(headNews)
                newsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                val toast = Toast.makeText(this@FeedActivity, t.toString(), Toast.LENGTH_SHORT)
                toast.show()
            }

        })
    }

    fun latestNews() {
        getNews().enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var lateNews = response.body()
                jsonLatest(lateNews)
                newsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {

            }

        })
    }

    private fun getHeader() = client.getHead()
    private fun getNews() = client.getNews()

    class mCoinClient {
        private val builder = Retrofit
                .Builder()
                .baseUrl("https://api.cointelegraph.com/")
                .addConverterFactory(ScalarsConverterFactory.create())

        private val retrofit: Retrofit by lazy {
            builder.build()
        }

        private val client: CoinClient by lazy {
            retrofit.create(CoinClient::class.java)
        }

        fun build() = client
    }

    fun jsonLatest(jsonString: String) {

        var latestNews = jsonString.substringAfter("\"type\":\"latest\",\"data\":")
        latestNews = latestNews.substringBefore("}]}}")
        val listNews = gson.fromJson(latestNews, Array<News>::class.java).asList()

        news.addAll(listNews)
    }

    fun jsonHeader(jsonString: String) {

        var headerNews = jsonString.substringAfter("\"type\":\"header\",\"data\":")
        headerNews = headerNews.substringBefore("},{\"type\":")
        val header: News = gson.fromJson(headerNews, News::class.java)

        news.add(header)

    }

}
