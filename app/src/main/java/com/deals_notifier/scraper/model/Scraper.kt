package com.deals_notifier.scraper.model

import android.util.Log
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import java.io.Serializable
import java.net.URL
import java.util.*

abstract class Scraper : Serializable {

    protected open var mostRecentPostDate: Date? = null

    /**
     * Gets all the posts
     */
    abstract suspend fun getAllPosts(): SortedPostList

    /**
     * Gets all the new posts that have appeared after this function was last called
     */
    abstract suspend fun getNewPosts(): SortedPostList

    /**
     * Resets the state of the scraper
     */
    fun reset() {
        mostRecentPostDate = null
    }

    protected suspend fun getData(url: URL): String {
        val response: String
        response = try {
            url.readText()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error getting response: $e")
            ""
        }
        return response
    }

}