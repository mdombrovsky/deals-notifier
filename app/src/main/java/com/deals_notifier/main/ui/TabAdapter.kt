package com.deals_notifier.main.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deals_notifier.main.controller.TabController

class TabAdapter(private val controller: TabController) : FragmentStateAdapter(controller.activity) {

//    private val queryHolder = QueryHolder()

//    lateinit var controller: TabController

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> controller.getDealFragment()
            else -> controller.getQueryFragment()
        }
    }
}