package com.hfad.cointask

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.model.News
import com.hfad.cointask.service.CoinClient
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_feed.*
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FeedActivity : AppCompatActivity() {

    var news = mutableListOf<News>()
    val gson = GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS").create()
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: CoinAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val client = mCoinClient().build()
    var offset = 0
    var length = 10
    private lateinit var compositeDispossable: CompositeDisposable
    private lateinit var pagination: PublishProcessor<Int>
    private val TAG = "FeedActivity"
    private var totalItemCount = 0
    private var lastVisibleItem = 0
    private var loading = false
    private val VISIBLE_TRESHOLD = 5
    private lateinit var progressBar: ProgressBar

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

        progressBar = findViewById(R.id.progressBar)
        newsRecyclerView = findViewById(R.id.news_recycler_view)
        newsRecyclerView.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        newsRecyclerView.layoutManager = layoutManager
        newsAdapter = CoinAdapter(news)
        newsRecyclerView.adapter = newsAdapter
        compositeDispossable = CompositeDisposable()
        pagination = PublishProcessor.create()

        news.clear()

        headerNews()
        setUpLoadMoreListener()
        subscribeForData()


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun setUpLoadMoreListener() {
        newsRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = newsRecyclerView.layoutManager!!.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_TRESHOLD)) {

                    offset += length
                    pagination.onNext(offset)
                    loading = true

                }

            }

        })
    }

    fun subscribeForData() {
        var disposable:Disposable = pagination
                .onBackpressureDrop()
                .concatMap {
                    loading = true
                    progressBar.visibility = View.VISIBLE
                     getNews(offset)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ t ->
                    jsonLatest(t)
                    newsAdapter.notifyDataSetChanged()
                    loading = false
                    progressBar.visibility = View.INVISIBLE
                }
                ,{
                    Log.e(TAG, it.toString())
                })

        compositeDispossable.add(disposable)
        pagination.onNext(offset)
    }

    fun headerNews() {
        getHeader().enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var headNews = response.body()
                if (headNews != null) {
                    jsonHeader(headNews)
                }
                newsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                val toast = Toast.makeText(this@FeedActivity, t.toString(), Toast.LENGTH_SHORT)
                toast.show()
            }

        })
    }

    private fun getHeader() = client.getHead()

    private fun getNews(offset: Int): Flowable<String> {
        return client.getNews(offset, length)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    class mCoinClient {
        private val builder = Retrofit
                .Builder()
                .baseUrl("https://api.cointelegraph.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

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

    override fun onDestroy() {
        super.onDestroy()
        compositeDispossable.dispose()
    }

}
