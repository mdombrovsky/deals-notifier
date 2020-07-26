package com.deals_notifier.query.model

import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter

class QueryController(private val queryAdapter: QueryAdapter, private val queries: ArrayList<Query>) {


    init {
        queryAdapter.controller = this
    }

    fun getSize(): Int {
        return queries.size
    }

    fun getQueryTitle(position: Int): String {
        return queries[position].title
    }

    fun createCriteriaAdapter(position: Int): CriteriaAdapter {
        val adapter = CriteriaAdapter()
        val controller = CriteriaController(adapter, queries[position].criteria)

        return adapter
    }
}