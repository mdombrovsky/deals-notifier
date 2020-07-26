package com.deals_notifier.query.model

import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.KeywordAdapter

class CriteriaController(private val criteriaAdapter:CriteriaAdapter, private val criteria: ArrayList<Criteria>) {

    init{
        criteriaAdapter.controller = this
    }

    fun getSize(): Int {
        return criteria.size
    }

    fun createKeyWordAdapter(position: Int): KeywordAdapter {
        val adapter = KeywordAdapter()
        val controller = KeywordController(adapter, criteria[position].keywords)

        return adapter
    }

}