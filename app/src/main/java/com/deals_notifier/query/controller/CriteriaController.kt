package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.KeywordAdapter

class CriteriaController(
    private val criteriaHolder: Query,
    private val onModified: () -> Unit
) {

    val criteriaAdapter: CriteriaAdapter = CriteriaAdapter(this)


    fun getSize(): Int {
        return criteriaHolder.criteria.size
    }

    fun add() {
        criteriaHolder.criteria.add(Criteria())
        criteriaAdapter.notifyItemInserted(criteriaHolder.criteria.size - 1)
        onModified()

    }

    fun createKeyWordAdapter(position: Int): KeywordAdapter {
        return KeywordController(criteriaHolder.criteria[position], onModified).keywordAdapter

    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            criteriaHolder.criteria.removeAt(position)
            criteriaAdapter.notifyItemRemoved(position)
            onModified()

        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }


}