package com.deals_notifier.query.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Keyword
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryController
import kotlinx.android.synthetic.main.fragment_deal.view.*
import kotlinx.android.synthetic.main.fragment_query.view.*

class QueryFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_query, container, false)
        implementRecyclerView(view)

        return view

    }

    private fun implementRecyclerView(view: View) {

        val queries = ArrayList<Query>()

        val queryAdapter = QueryAdapter()

        val queryController = QueryController(queryAdapter, queries)

        val recyclerView: RecyclerView = view.queryRecyclerView
        recyclerView.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }
}