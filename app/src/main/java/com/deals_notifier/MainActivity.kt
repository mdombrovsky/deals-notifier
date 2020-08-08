package com.deals_notifier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.main.controller.TabController
import com.deals_notifier.query.model.QueryHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewPager2: ViewPager2 = findViewById(R.id.view_pager)


        viewPager2.adapter = TabController(
            this,
            this.intent.getSerializableExtra(ValidDealHolder::class.java.simpleName) as ValidDealHolder
        ).tabAdapter


        val tabs: TabLayout = findViewById(R.id.tabs)

        val tabLayoutMediator = TabLayoutMediator(tabs, viewPager2,
            TabLayoutMediator.TabConfigurationStrategy() { tab: TabLayout.Tab, i: Int ->
                when (i) {
                    0 -> tab.text = "Deals"
                    1 -> tab.text = "Queries"
                }
            })

        tabLayoutMediator.attach()


    }
}