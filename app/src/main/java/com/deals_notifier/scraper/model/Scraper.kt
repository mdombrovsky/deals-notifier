package com.deals_notifier.scraper.model

import android.util.Log
import com.deals_notifier.post.model.Post
import java.io.Serializable
import java.net.URL

abstract class Scraper : Serializable {

    protected var mostRecentPostId: String? = null

    /**
     * Gets all the posts
     */
    abstract suspend fun getAllPosts(): List<Post>

    /**
     * Gets all the new posts that have appeared after this function was last called
     */
    abstract suspend fun getNewPosts(): List<Post>

    /**
     * Resets the state of the scraper
     */
    fun reset() {
        mostRecentPostId = null
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