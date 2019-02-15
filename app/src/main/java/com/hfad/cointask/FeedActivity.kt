package com.hfad.cointask

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.cointask.fragments.FeedFragment
import com.hfad.cointask.fragments.SavedFragment
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private var fragmentMain = androidx.fragment.app.Fragment()




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

        val tx: androidx.fragment.app.FragmentTransaction =  supportFragmentManager.beginTransaction()
        tx.replace(R.id.main_fragment, FeedFragment())
        tx.commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


    fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.main_fragment, f)
        ft.commit()

    }

}
