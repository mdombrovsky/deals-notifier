package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

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
            posts.add(createRedditPost(jsonPost))
        }

        //Remember most recent post
        if (posts.size > 1) {
            mostRecentPostId = posts[0].id
        }

        return posts
    }


    private fun createRedditPost(jsonPost: JSONObject): Post {
        if (jsonPost.getString("kind") == "t3") {
            val jsonPostData: JSONObject = jsonPost.getJSONObject("data")

            val id = jsonPostData.getString("id")
            return (Post(
                title = jsonPostData.getString("title"),
                description = jsonPostData.getString("selftext"),
                id = id,
                url = URL("https://www.reddit.com/$id"),
                date = Date(jsonPostData.getLong("created_utc") * 1000)
            ))


        } else {
            throw JSONException("Incorrect Format for Reddit Post")
        }
    }


    override suspend fun getPosts(): List<Post> {
        val url: URL = URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100")

        return redditJSONToPosts(getData(url))

    }

    override suspend fun getNewPosts(): List<Post> {
        if (mostRecentPostId == null || mostRecentPostId!!.isEmpty()) {
            return getPosts()
        }
        val url: URL =
            URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100&before=t3_${mostRecentPostId}")

        return redditJSONToPosts(getData(url))
    }
}