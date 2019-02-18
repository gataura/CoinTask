package com.hfad.cointask.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.hfad.cointask.R
import com.hfad.cointask.adapter.SavedAdapter
import com.hfad.cointask.helper.AppDatabase
import com.hfad.cointask.model.News
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SavedFragment : androidx.fragment.app.Fragment() {

    lateinit var db: AppDatabase
    private var news = mutableListOf<News>()
    private lateinit var savedRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var savedAdapter: SavedAdapter
    private lateinit var layoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("WrongConstant", "CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_saved, container, false)

        savedRecyclerView = view.findViewById(R.id.saved_recycler_view)
        savedRecyclerView.isNestedScrollingEnabled = false

        mSwipeRefreshLayout = view.findViewById(R.id.saved_container)

       if (isAdded) {
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

        savedRecyclerView.layoutManager = layoutManager

        savedAdapter = SavedAdapter(news, this.requireContext())

        savedRecyclerView.adapter = savedAdapter
        news.clear()

        Observable.fromCallable{db.newsDao().getAll()}
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe({
                       news.clear()
                       news.addAll(it)
                       savedAdapter.notifyDataSetChanged()
                   } , {
                       Log.d("SavedFragment", "Распарси", it)
                   })


        mSwipeRefreshLayout.setOnRefreshListener {
            Observable.fromCallable{db.newsDao().getAll()}
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        news.clear()
                        news.addAll(it)
                        savedAdapter.notifyDataSetChanged()
                    } , {
                        Log.d("SavedFragment", "Распарси", it)
                    })
            mSwipeRefreshLayout.isRefreshing = false
        }
       }

        return view
    }


}
