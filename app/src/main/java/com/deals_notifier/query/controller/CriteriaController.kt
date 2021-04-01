package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.ui.CriteriaAdapter
import com.deals_notifier.query.ui.KeywordAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CriteriaController(
    private val criteriaHolder: Query,
    private val onModified: () -> Unit
) {

    private val criteriaAdapter: CriteriaAdapter = CriteriaAdapter(this)


    fun getSize(): Int {
        return criteriaHolder.criteria.size
    }

    fun add() {
        CoroutineScope(IO).launch {
            criteriaHolder.addCriteria(Criteria())
            withContext(Main) {
                criteriaAdapter.notifyItemInserted(criteriaHolder.criteria.size - 1)
                onModified()
            }
        }

    }

    fun createKeyWordAdapter(position: Int): KeywordAdapter {
        return KeywordController(criteriaHolder.criteria[position], onModified).keywordAdapter

    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            CoroutineScope(IO).launch {
                criteriaHolder.removeCriteriaAt(position)

                withContext(Main) {
                    criteriaAdapter.notifyItemRemoved(position)
                    onModified()
                }
            }
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }


}