package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.KeywordAdapter

class CriteriaController(
    private val criteriaAdapter: CriteriaAdapter,
    private val criteriaHolder: Query
) {


    init {
        criteriaAdapter.controller = this
    }

    fun getSize(): Int {
        return  criteriaHolder.criteria.size
    }

    fun add() {
        criteriaHolder.criteria.add(Criteria())
        criteriaAdapter.notifyItemInserted( criteriaHolder.criteria.size - 1)
    }

    fun createKeyWordAdapter(position: Int): KeywordAdapter {
        val adapter = KeywordAdapter()
        val controller = KeywordController(
            adapter,
            criteriaHolder.criteria[position]
        )

        return adapter
    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            criteriaHolder.criteria.removeAt(position)
            criteriaAdapter.notifyItemRemoved(position)
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }


}