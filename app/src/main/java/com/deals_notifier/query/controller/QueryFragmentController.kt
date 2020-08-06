package com.deals_notifier.query.controller

import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Keyword
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.query.ui.QueryAdapter
import com.deals_notifier.query.ui.QueryFragment

class QueryFragmentController(
    private val queryFragment: QueryFragment,
    private val queryHolder: QueryHolder
) {

    init {
        queryFragment.controller = this
    }

    fun createQueryAdapter(): QueryAdapter {
        val adapter = QueryAdapter()
        val controller = QueryController(queryHolder = queryHolder, queryAdapter = adapter)
        return adapter
    }

    fun createTestData() {
        val keyword = Keyword("test 1")
        val keyword2 = Keyword("test 2")
        val keyword3 = Keyword("test 3")
        val criteria = Criteria()
        criteria.keywords.add(keyword)
        criteria.keywords.add(keyword2)
        val criteria2 = Criteria()
        criteria2.keywords.add(keyword3)
        val query = Query()
        query.title = "query1"
        query.criteria.add(criteria)
        query.criteria.add(criteria2)
        val query2 = Query()
        query2.title = "query2"

        query2.criteria.add(criteria2)

        queryHolder.queries.add(query)
        queryHolder.queries.add(query2)

    }

}