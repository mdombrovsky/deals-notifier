package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

class RedditScraper(private val subReddit: String) : Scraper() {

    constructor(json: JSONObject) : this(initScraperFromJSON(json))

    companion object {
        private const val dataTypeJSON = "subReddit"
        
        private fun initScraperFromJSON(json: JSONObject): String {
            return json.getString(dataTypeJSON)
        }
    }

    override suspend fun getAllPosts(): SortedPostList {
        return redditJSONToPosts(
            getData(
                URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100")
            )
        )
    }

    override suspend fun getNewPosts(): SortedPostList {
        val posts =
            redditJSONToPosts(
                getData(
                    URL("https://www.reddit.com/r/${subReddit}/new.json?limit=100")
                )
            ).also {
                it.removeAllOlderThan(date = mostRecentPostDate)
            }

        //Remember most recent post
        if (posts.isNotEmpty()) {
            mostRecentPostDate = posts[0].date
        }

        return posts
    }

    override fun getName(): String {
        return "Reddit, subreddit = $subReddit"
    }

    /**
     * Converts reddit json into posts
     *
     * @param jsonString The reddit json
     */
    private fun redditJSONToPosts(jsonString: String): SortedPostList {
        val posts = SortedPostList()

        val json = JSONObject(jsonString)

        if (json.getString("kind") != "Listing") {
            return posts
        }

        val jsonPostArray: JSONArray = json.getJSONObject("data").getJSONArray("children")

        for (i in 0 until jsonPostArray.length()) {
            val jsonPost: JSONObject = jsonPostArray.getJSONObject(i)
            val post: Post = createRedditPost(jsonPost)


            posts.add(post)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RedditScraper

        if (subReddit != other.subReddit) return false

        return true
    }

    override fun toJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(scraperTypeJSON, redditScraperName)

        val dataJSONObject= JSONObject()
        dataJSONObject.put(dataTypeJSON, subReddit)

        jsonObject.put(dataNameJSON, dataJSONObject)
        return jsonObject
    }

    override fun hashCode(): Int {
        return subReddit.hashCode()
    }


}