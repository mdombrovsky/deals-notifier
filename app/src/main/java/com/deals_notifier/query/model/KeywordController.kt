package com.deals_notifier.query.model

import com.deals_notifier.query.ui.KeywordAdapter

class KeywordController(private val keywordAdapter: KeywordAdapter, private val keywords: ArrayList<Keyword>) {


    init{
        keywordAdapter.controller = this
    }

    fun getSize(): Int {
        return keywords.size
    }

    fun getKeyWord(position:Int):String{
        return keywords[position].text
    }
}