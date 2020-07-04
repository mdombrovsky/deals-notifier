package com.deals_notifier.post.model

import org.json.JSONException
import org.json.JSONObject
import java.net.URL

class RedditPost(jsonPost: JSONObject) : Post() {

    override val title: String
    override val description: String
    override val id: String
    override val url: URL

    override val stringToSearchNoSpaces: String

    init {
        if (jsonPost.getString("kind") == "t3") {
            val jsonPostData: JSONObject = jsonPost.getJSONObject("data")

            title = jsonPostData.getString("title")
            description = jsonPostData.getString("selftext")
            id = jsonPostData.getString("id")
            url = URL("https://www.reddit.com/$id")

            stringToSearchNoSpaces =
                title.replace("\\s".toRegex(), "") + description.replace("\\s".toRegex(), "")

        } else {
            throw JSONException("Incorrect Format for Reddit Post")
        }
    }

}