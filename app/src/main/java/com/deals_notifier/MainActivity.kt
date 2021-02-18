package com.deals_notifier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.deals_notifier.main.controller.TabController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    companion object {
        var mainActivity: MainActivity? = null
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager2: ViewPager2 = findViewById(R.id.view_pager)

        val tabAdapter = TabController(this).tabAdapter

        viewPager2.adapter = tabAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        val tabLayoutMediator = TabLayoutMediator(tabs, viewPager2, tabAdapter)

        tabLayoutMediator.attach()
    }


}