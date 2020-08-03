package com.deals_notifier.query.model

import android.util.Log
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.KeywordAdapter

class CriteriaController(
    private val criteriaAdapter: CriteriaAdapter,
    private val criteria: ArrayList<Criteria>
) {


    init {
        criteriaAdapter.controller = this
    }

    fun getSize(): Int {
        return criteria.size
    }

    fun add() {
        criteria.add(Criteria())
        criteriaAdapter.notifyItemInserted(criteria.size - 1)
    }

    fun createKeyWordAdapter(position: Int): KeywordAdapter {
        val adapter = KeywordAdapter()
        val controller = KeywordController(adapter, criteria[position].keywords)

        return adapter
    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            criteria.removeAt(position)
            criteriaAdapter.notifyItemRemoved(position)
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }


}