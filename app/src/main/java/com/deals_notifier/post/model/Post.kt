package com.deals_notifier.post.model

import com.deals_notifier.query.model.Query
import java.net.URL

abstract class Post() {


    abstract val title: String
    abstract val description: String
    abstract val id: String
    abstract val url: URL

    protected abstract val stringToSearchNoSpaces: String

    fun contains(searchString: String): Boolean {

        if (searchString.contains("\\s".toRegex())) {
            throw Exception("Search string must have no spaces")
        }
        return stringToSearchNoSpaces.contains(searchString)
    }

    fun matchesQuery(query:Query):Boolean{
        TODO("Not implemented yet")
        return false
    }

    override fun toString(): String {
        return "Title: {$title}, Description: {$description}\n"
    }

}