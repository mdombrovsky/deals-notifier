package com.deals_notifier.query.model

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter

class QueryController(
    private val queryAdapter: QueryAdapter,
    private val queries: ArrayList<Query>
) {

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

    fun setQueryTitle(position: Int, title: String) {
        queries[position].title = title
        queryAdapter.notifyItemChanged(position)
    }

    fun add(title: String) {
        queries.add(Query(title = title))
        queryAdapter.notifyItemInserted(queries.size - 1)
    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            queries.removeAt(position)
            queryAdapter.notifyItemRemoved(position)
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to edit item that is not present"
            )
        }
    }

}