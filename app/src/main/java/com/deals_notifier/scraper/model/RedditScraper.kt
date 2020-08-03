package com.deals_notifier.scraper.model

import android.util.Log
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.RedditPost
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class RedditScraper(private val subReddit: String) : Scraper() {

    private var mostRecentPostId: String? = null

    private fun redditJSONToPosts(jsonString: String): List<Post> {
        val posts: ArrayList<Post> = ArrayList<Post>()

        val json = JSONObject(jsonString)

        if (json.getString("kind") != "Listing") {
            return posts
        }

        val jsonPostArray: JSONArray = json.getJSONObject("data").getJSONArray("children")

        for (i in 0 until jsonPostArray.length()) {
            val jsonPost: JSONObject = jsonPostArray.getJSONObject(i)
            posts.add(RedditPost(jsonPost))
        }

        //Remember most recent post
        if (posts.size > 1) {
            mostRecentPostId = posts[0].id
        }

        return posts
    }

    private fun getRedditData(url: URL): String {
        val response: String
        response = try {
            url.readText()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error getting response: $e")
            ""
        }
        return response
    }

    public override suspend fun getPosts(): List<Post> {
        val url: URL = URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100")

        return redditJSONToPosts(getRedditData(url))

    }

    override suspend fun getNewPosts(): List<Post> {
        if (mostRecentPostId == null || mostRecentPostId!!.isEmpty()) {
            return getPosts()
        }
        val url: URL =
            URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100&before=t3_${mostRecentPostId}")

        return redditJSONToPosts(getRedditData(url))
    }
}