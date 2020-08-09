package com.deals_notifier.post.model

import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*


fun createRedditPost(jsonPost: JSONObject): Post {
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

