package com.deals_notifier.scraper.model

import android.util.Log
import com.deals_notifier.post.model.Post
import java.io.Serializable
import java.net.URL

abstract class Scraper : Serializable {
    abstract suspend fun getPosts(): List<Post>
    abstract suspend fun getNewPosts(): List<Post>

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