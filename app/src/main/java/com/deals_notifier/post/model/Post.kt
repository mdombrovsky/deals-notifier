package com.deals_notifier.post.model

import java.net.URL

abstract class Post() {


    abstract val title: String
    abstract val description: String
    abstract val id: String
    abstract val url: URL

    protected abstract val stringToSearchNoSpacesLowercase: String

    fun contains(searchString: String): Boolean {

        if (searchString.contains("\\s".toRegex())) {
            throw Exception("Search string must have no spaces")
        }
        return stringToSearchNoSpacesLowercase.contains(searchString)
    }

    override fun toString(): String {
        return "Title: {$title}, Description: {$description}\n"
    }

}