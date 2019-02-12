package com.hfad.cointask

import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.NavigationMenuItemView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.hfad.cointask.adapter.CoinAdapter
import com.hfad.cointask.fragments.FeedFragment
import com.hfad.cointask.fragments.SavedFragment
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

    var fragmentMain = Fragment()



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_news -> {
                fragmentMain = FeedFragment()
                setFragment(fragmentMain)


                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_saved -> {
                fragmentMain = SavedFragment()
                setFragment(fragmentMain)


                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val tx: FragmentTransaction =  supportFragmentManager.beginTransaction()
        tx.replace(R.id.main_fragment, FeedFragment())
        tx.commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    fun setFragment(f: Fragment) {

        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.main_fragment, f)
        ft.commit()

    }

}
