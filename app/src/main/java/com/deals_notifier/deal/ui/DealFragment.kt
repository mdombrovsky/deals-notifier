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


class DealFragment(val controller: DealFragmentController) : Fragment() {

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal, container, false)


        val newAdapter = controller.getDealAdapter()
        implementSwipeRefresh(view)
        implementRecyclerView(view, newAdapter)

        controller.notifyViewCreated()

        return view
    }

    private fun implementSwipeRefresh(view: View) {
        swipeRefreshLayout = view.refreshDeal
        swipeRefreshLayout.setOnRefreshListener { controller.refresh(false) }
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