package com.deals_notifier.post.model

import android.util.Log
import java.io.Serializable
import java.net.URL
import java.util.*

class Post(
    val title: String = "",
    private val description: String = "",
    val id: String,
    val url: URL? = null,
    val date: Date
) : Serializable, Comparable<Post> {


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

    override fun compareTo(other: Post): Int {
        val result = other.date.compareTo(this.date)
        if (result == 0) {
            return this.id.compareTo(other.id)

        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}