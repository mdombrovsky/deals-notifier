package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueryController(
    private val queryHolder: QueryHolder,
    private val onModified: () -> Unit
) {
    val queryAdapter: QueryAdapter = QueryAdapter(this)


    fun getSize(): Int {
        return queryHolder.queries.size
    }

    fun getQueryTitle(position: Int): String {
        return queryHolder.queries[position].title
    }

    fun createCriteriaAdapter(position: Int): CriteriaAdapter {
        return CriteriaController(
            queryHolder.queries[position],
            onModified
        ).criteriaAdapter

    }

    fun setQueryTitle(position: Int, title: String) {
        queryHolder.queries[position].title = title
        queryAdapter.notifyItemChanged(position)
        onModified()
    }

    fun add(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            queryHolder.addQuery(Query(title = title))
            withContext(Dispatchers.Main) {
                queryAdapter.notifyItemInserted(queryHolder.queries.size - 1)
                onModified()
            }
        }

    }

    fun remove(query: Query) {
        CoroutineScope(Dispatchers.IO).launch {
            val success: Boolean = queryHolder.removeQuery(query)

            if (success) {
                withContext(Dispatchers.Main) {
                    queryAdapter.notifyDataSetChanged()
                    onModified()
                }
            } else {
                Log.e(
                    this.javaClass.simpleName,
                    "Error removing query"
                )
            }
        }
    }

    fun getQuery(position: Int): Query {
        return queryHolder.queries[position]
    }


}