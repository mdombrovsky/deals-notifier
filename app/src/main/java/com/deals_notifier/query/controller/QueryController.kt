package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter

class QueryController(
    private val queryAdapter: QueryAdapter,
    private val queryHolder: QueryHolder,
    private val onModified: ()->Unit
) {

    init {
        queryAdapter.controller = this
    }

    fun getSize(): Int {
        return queryHolder.queries.size
    }

    fun getQueryTitle(position: Int): String {
        return queryHolder.queries[position].title
    }

    fun createCriteriaAdapter(position: Int): CriteriaAdapter {
        val adapter = CriteriaAdapter()
        val controller = CriteriaController(
            adapter,
            queryHolder.queries[position],
            onModified
        )

        return adapter
    }

    fun setQueryTitle(position: Int, title: String) {
        queryHolder.queries[position].title = title
        queryAdapter.notifyItemChanged(position)
        onModified()
    }

    fun add(title: String) {
        queryHolder.queries.add(Query(title = title))
        queryAdapter.notifyItemInserted(queryHolder.queries.size - 1)
        onModified()

    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            queryHolder.queries.removeAt(position)
            queryAdapter.notifyItemRemoved(position)
            onModified()

        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to edit item that is not present"
            )
        }
    }



}