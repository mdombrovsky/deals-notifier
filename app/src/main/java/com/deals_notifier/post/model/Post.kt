package com.deals_notifier.post.model

import android.util.Log
import java.net.URL
import java.util.*

class Post(val title: String, private val description: String, val id: String, val url: URL) {


    private val stringToSearchNoSpacesLowercase: String = (
            title.replace("\\s+".toRegex(), "") +
                    description.replace("\\s+".toRegex(), "")
            ).toLowerCase(Locale.ENGLISH)


    init{
        Log.d(this.javaClass.simpleName, "Post loaded: ${this.toString()}")
    }

    fun contains(searchString: String): Boolean {
        return stringToSearchNoSpacesLowercase.contains(searchString)
    }

    override fun toString(): String {
        return "Title: {$title}, Description: {$description}\n"
    }

}