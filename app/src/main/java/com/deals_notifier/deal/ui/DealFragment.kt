package com.deals_notifier.deal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.deals_notifier.R
import com.deals_notifier.deal.model.DealController
import kotlinx.android.synthetic.main.fragment_deal.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class DealFragment(activity: AppCompatActivity) : Fragment() {
    private val dealAdapter: DealAdapter = DealAdapter()
    private val dealController: DealController = DealController(dealAdapter)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal, container, false)

        implementSwipeRefresh(view)
        implementRecyclerView(view)

        return view
    }

    private fun implementSwipeRefresh(view: View) {
        val swipeRefreshLayout: SwipeRefreshLayout = view.refreshDeal
        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(IO).launch {
                dealController.refresh()
            }.invokeOnCompletion {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun implementRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.dealRecyclerView
        recyclerView.apply {
            adapter = dealAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


}