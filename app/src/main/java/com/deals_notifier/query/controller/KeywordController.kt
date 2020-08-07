package com.deals_notifier.query.controller

import android.util.Log
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Keyword
import com.deals_notifier.query.ui.KeywordAdapter

class KeywordController(
    private val keywordAdapter: KeywordAdapter,
    private val keywordHolder: Criteria,
    private val onModified: () ->Unit
) {


    init {
        keywordAdapter.controller = this
    }

    fun getSize(): Int {
        return keywordHolder.keywords.size
    }

    fun getKeyWord(position: Int): String {
        return keywordHolder.keywords[position].text
    }

    fun add(text: String) {
        keywordHolder.keywords.add(Keyword(text))
        keywordAdapter.notifyItemInserted(getSize() - 1)
        onModified()
    }

    fun remove(position: Int) {
        //Trying to preemptively avoid issues with getLayoutPosition vs getAdapterPosition
        if (position != -1) {
            keywordHolder.keywords.removeAt(position)
            keywordAdapter.notifyItemRemoved(position)
            onModified()
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
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
}