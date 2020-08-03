package com.deals_notifier.post.model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

class RedditPost(jsonPost: JSONObject) : Post() {

    override val title: String
    override val description: String
    override val id: String
    override val url: URL

    override val stringToSearchNoSpacesLowercase: String

    init {
        if (jsonPost.getString("kind") == "t3") {
            val jsonPostData: JSONObject = jsonPost.getJSONObject("data")

            title = jsonPostData.getString("title")
            description = jsonPostData.getString("selftext")
            id = jsonPostData.getString("id")
            url = URL("https://www.reddit.com/$id")

            stringToSearchNoSpacesLowercase =
                (title.replace("\\s".toRegex(), "") + description.replace(
                    "\\s".toRegex(),
                    ""
                )).toLowerCase(
                    Locale.ENGLISH
                )

            Log.d(this.javaClass.simpleName, "Post loaded: ${this.toString()}")

        } else {
            throw JSONException("Incorrect Format for Reddit Post")
        }
    }

}