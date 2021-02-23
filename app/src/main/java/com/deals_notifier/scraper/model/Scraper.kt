package com.deals_notifier.scraper.model

import android.util.Log
import com.deals_notifier.post.model.SortedPostList
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.net.URL
import java.util.*

abstract class Scraper : Serializable {

    companion object {
        const val scraperTypeJSON = "type"
        const val dataNameJSON = "data"
        const val redditScraperName = "reddit"
        const val rfdScraperName = "rfd"

        fun getScraperFromJSON(json: JSONObject): Scraper {
            val type = json.getString(scraperTypeJSON)
            val data = json.getJSONObject(dataNameJSON)
            return when (type) {
                redditScraperName -> RedditScraper(data)
                rfdScraperName -> RFDScraper(data)
                else -> throw JSONException("Unknown Scraper Type")
            }

        }
    }


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
     * The displayable name of this scraper
     */
    abstract fun getName(): String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    /**
     * Resets the state of the scraper
     */
    fun reset() {
        mostRecentPostDate = null
    }

    protected fun getData(url: URL): String {
        val response: String
        response = try {
            url.readText()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error getting response: $e")
            ""
        }
        return response
    }

    abstract fun toJSON(): JSONObject
}