package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter

class QueryController(
    private val onModified: () -> Unit
) {
    val queryAdapter: QueryAdapter = QueryAdapter(this)


    fun getSize(): Int {
        return DealService.dealManager!!.queryHolder.queries.size
    }

    fun getQueryTitle(position: Int): String {
        return DealService.dealManager!!.queryHolder.queries[position].title
    }

    fun createCriteriaAdapter(position: Int): CriteriaAdapter {
        return CriteriaController(DealService.dealManager!!.queryHolder.queries[position], onModified).criteriaAdapter

    }

    fun setQueryTitle(position: Int, title: String) {
        DealService.dealManager!!.queryHolder.queries[position].title = title
        queryAdapter.notifyItemChanged(position)
        onModified()
    }

    fun add(title: String) {
        DealService.dealManager!!.queryHolder.queries.add(Query(title = title))
        queryAdapter.notifyItemInserted(DealService.dealManager!!.queryHolder.queries.size - 1)
        onModified()

    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            DealService.dealManager!!.queryHolder.queries.removeAt(position)
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