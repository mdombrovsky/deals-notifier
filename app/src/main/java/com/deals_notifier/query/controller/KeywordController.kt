package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Keyword
import com.deals_notifier.query.ui.KeywordAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KeywordController(

    private val keywordHolder: Criteria,
    private val onModified: () -> Unit
) {

    val keywordAdapter: KeywordAdapter = KeywordAdapter(this)

    fun getSize(): Int {
        return keywordHolder.keywords.size
    }

    fun getKeyWord(position: Int): String {
        return keywordHolder.keywords[position].text
    }

    fun add(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            keywordHolder.addKeyword(Keyword(text))
            withContext(Main) {
                keywordAdapter.notifyItemInserted(getSize() - 1)
                onModified()
            }
        }
    }

    fun remove(keyword: Keyword) {
        CoroutineScope(Dispatchers.IO).launch {
            val success: Boolean = keywordHolder.removeKeyword(keyword)

            if (success) {
                withContext(Main) {
                    keywordAdapter.notifyDataSetChanged()
                    onModified()
                }
            } else {
                Log.e(
                    this.javaClass.simpleName,
                    "Error removing keyword"
                )
            }
        }
    }

    fun edit(position: Int, text: String) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            keywordHolder.keywords[position].text = text
            keywordAdapter.notifyItemChanged(position)
            onModified()
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to edit item that is not present"
            )
        }
    }

    fun getKeyword(position: Int): Keyword {
        return keywordHolder.keywords[position]
    }
}