package com.deals_notifier.main.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.query.ui.QueryFragment

class TabAdapter(private val activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DealFragment(activity)

            }
            else -> QueryFragment()
        }
    }
}