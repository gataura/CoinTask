package com.hfad.cointask.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.hfad.cointask.FeedActivity

import com.hfad.cointask.R
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.model.News
import com.hfad.cointask.service.CoinClient
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FeedFragment : androidx.fragment.app.Fragment() {

    private var news = mutableListOf<News>()
    private val gson = GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS").create()
    private lateinit var newsRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var newsAdapter: CoinAdapter
    private lateinit var layoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private val client = FeedFragment.mCoinClient().build()
    var offset = 0
    var length = 10
    private lateinit var compositeDispossable: CompositeDisposable
    private lateinit var pagination: PublishProcessor<Int>
    private val TAG = "FeedActivity"
    private var totalItemCount = 0
    private var lastVisibleItem = 0
    private var loading = false
    private val VISIBLE_TRESHOLD = 1
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_feed, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        newsRecyclerView = view.findViewById(R.id.news_recycler_view)
        newsRecyclerView.isNestedScrollingEnabled = false
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.requireContext(), androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        newsRecyclerView.layoutManager = layoutManager
        newsAdapter = CoinAdapter(news, this.requireContext())
        newsRecyclerView.adapter = newsAdapter
        compositeDispossable = CompositeDisposable()
        pagination = PublishProcessor.create()

        news.clear()

        headerNews()
        setUpLoadMoreListener()
        subscribeForData()

        return view
    }

    private fun setUpLoadMoreListener() {
        newsRecyclerView.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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

    private fun subscribeForData() {

        var disposable: Disposable = pagination
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

    private fun headerNews() {
        getHeader().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var headNews = response.body()
                if (headNews != null) {
                    jsonHeader(headNews)
                }
                newsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                val toast = Toast.makeText(FeedActivity(), t.toString(), Toast.LENGTH_SHORT)
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

    private fun jsonLatest(jsonString: String) {

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
