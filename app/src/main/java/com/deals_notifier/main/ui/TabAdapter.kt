package com.deals_notifier.main.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deals_notifier.main.controller.TabController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabAdapter(private val controller: TabController) : FragmentStateAdapter(controller.activity) ,TabLayoutMediator.TabConfigurationStrategy{


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> controller.getDealFragment()
            else -> controller.getQueryFragment()
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        when (position) {
            0 -> tab.text = "Deals"
            1 -> tab.text = "Queries"
        }
    }
}