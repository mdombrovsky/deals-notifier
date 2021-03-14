package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.QueryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QueryController(
    private val onModified: () -> Unit
) {
    val queryAdapter: QueryAdapter = QueryAdapter(this)


    fun getSize(): Int {
        return DealManager.instance!!.queryHolder.queries.size
    }

    fun getQueryTitle(position: Int): String {
        return DealManager.instance!!.queryHolder.queries[position].title
    }

    fun createCriteriaAdapter(position: Int): CriteriaAdapter {
        return CriteriaController(
            DealManager.instance!!.queryHolder.queries[position],
            onModified
        ).criteriaAdapter

    }

    fun setQueryTitle(position: Int, title: String) {
        DealManager.instance!!.queryHolder.queries[position].title = title
        queryAdapter.notifyItemChanged(position)
        onModified()
    }

    fun add(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DealManager.instance!!.queryHolder.addQuery(Query(title = title))
            withContext(Dispatchers.Main) {
                queryAdapter.notifyItemInserted(DealManager.instance!!.queryHolder.queries.size - 1)
                onModified()
            }
        }

    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                DealManager.instance!!.queryHolder.removeQueryAt(position)
                withContext(Dispatchers.Main) {
                    queryAdapter.notifyItemRemoved(position)
                    onModified()
                }
            }
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to edit item that is not present"
            )
        }
    }

    fun setQueryEnabled(position: Int, enabled: Boolean) {
        if (isQueryEnabled(position) != enabled) {
            CoroutineScope(Dispatchers.IO).launch {
                DealManager.instance!!.queryHolder.enableQueryAt(position, enabled)
                withContext(Dispatchers.Main) {
                    // For future, if we want to grey the query out
                    queryAdapter.notifyItemChanged(position)
                    onModified()
                }
            }
        }
    }

    fun isQueryEnabled(position: Int): Boolean {
        return DealManager.instance!!.queryHolder.queries[position].enabled
    }


}