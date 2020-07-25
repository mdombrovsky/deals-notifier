package com.deals_notifier.deal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.model.DealController
import kotlinx.android.synthetic.main.fragment_deal.*
import kotlinx.android.synthetic.main.fragment_deal.view.*


class DealFragment(activity: AppCompatActivity) : Fragment() {
    private val dealAdapter: DealAdapter = DealAdapter(activity)
    private val dealController: DealController = DealController(dealAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dealFragmentView = inflater.inflate(R.layout.fragment_deal, container, false)

        val recyclerView:RecyclerView = dealFragmentView.dealRecyclerView
        recyclerView.apply {
            adapter = dealAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        return dealFragmentView
    }
}