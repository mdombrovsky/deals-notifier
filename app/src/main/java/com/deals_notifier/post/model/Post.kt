package com.deals_notifier.post.model

import android.util.Log
import java.io.Serializable
import java.net.URL
import java.util.*

class Post(
    val title: String,
    private val description: String,
    val id: String,
    val url: URL,
    val date: Date
) : Serializable {


    private val stringToSearchNoSpacesLowercase: String = (
            title.replace("\\s+".toRegex(), "") +
                    description.replace("\\s+".toRegex(), "")
            ).toLowerCase(Locale.ENGLISH)


    init {
        Log.d(this.javaClass.simpleName, "Post loaded: ${this.toString()}")
    }

    fun contains(searchString: String): Boolean {
        return stringToSearchNoSpacesLowercase.contains(searchString)
    }

    override fun toString(): String {
        return "Title: {$title}, Date: {${date}}\n"
    }

}