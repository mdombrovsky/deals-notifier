package com.deals_notifier.main.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deals_notifier.main.controller.TabController
import com.deals_notifier.query.model.QueryHolder

class TabAdapter(private val activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val queryHolder = QueryHolder()

    lateinit var controller: TabController

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> controller.createDealFragment()
            else -> controller.createQueryFragment()
        }
    }
}