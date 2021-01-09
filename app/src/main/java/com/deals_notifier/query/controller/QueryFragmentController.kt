package com.deals_notifier.query.controller

import android.content.Context
import com.deals_notifier.deal.model.DealManager
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
            queryHolder = DealManager.instance!!.queryHolder,
            onModified = { queryModified() }).queryAdapter
    }

    private fun queryModified() {
        CoroutineScope(IO).launch {
            DealManager.instance!!.queryHolder.save(context)
        }
        onModified()
    }

}