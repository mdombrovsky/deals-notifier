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

    val criteriaAdapter: CriteriaAdapter = CriteriaAdapter(this)


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

    fun remove(criteria: Criteria) {
        CoroutineScope(IO).launch {
            val success: Boolean = criteriaHolder.removeCriteria(criteria)
            if (success) {
                withContext(Main) {
                    criteriaAdapter.notifyDataSetChanged()
                    onModified()
                }
            } else {
                Log.e(
                    this.javaClass.simpleName,
                    "Error removing criteria"
                )
            }
        }
    }

    fun getCriteria(position: Int): Criteria {
        return criteriaHolder.criteria[position]
    }


}