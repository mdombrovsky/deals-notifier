package com.deals_notifier.query.controller

import android.content.Context
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.query.ui.QueryAdapter
import com.deals_notifier.query.ui.QueryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class QueryFragmentController(
    private val context: Context,
    private val onModified: () -> Unit
) {

    val queryFragment: QueryFragment = QueryFragment(this)

    fun createQueryAdapter(): QueryAdapter {
        return QueryController(
            onModified = { queryModified() }).queryAdapter
    }

    private fun queryModified() {
        CoroutineScope(IO).launch {
            DealService.dealManager!!.queryHolder.save(context)
        }
        onModified()
    }

}