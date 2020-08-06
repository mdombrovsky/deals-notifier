package com.deals_notifier.deal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.deals_notifier.R
import com.deals_notifier.deal.controller.DealFragmentController
import kotlinx.android.synthetic.main.fragment_deal.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class DealFragment() : Fragment() {

    lateinit var controller: DealFragmentController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal, container, false)


        val newAdapter = controller.createDealAdapter()
        implementSwipeRefresh(view, newAdapter)
        implementRecyclerView(view, newAdapter)


        return view
    }

    private fun implementSwipeRefresh(
        view: View,
        newAdapter: DealAdapter
    ) {
        val swipeRefreshLayout: SwipeRefreshLayout = view.refreshDeal
        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(IO).launch {
                newAdapter.controller.refresh()
            }.invokeOnCompletion {
                swipeRefreshLayout.isRefreshing = false
            }
        }

    }

    private fun implementRecyclerView(
        view: View,
        newAdapter: DealAdapter
    ) {

        val recyclerView: RecyclerView = view.dealRecyclerView
        recyclerView.apply {
            adapter = newAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


}