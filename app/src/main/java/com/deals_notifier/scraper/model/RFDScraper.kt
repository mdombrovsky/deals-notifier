package com.deals_notifier.scraper.model

import android.annotation.SuppressLint
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.text.SimpleDateFormat

class RFDScraper(private val category: Int) : Scraper() {
    //Category: 0 -> All, 9 -> Computers & Electronics

    constructor(json: JSONObject) : this(initScraperFromJSON(json))

    private companion object {
        private const val dataTypeJSON = "category"

        private fun initScraperFromJSON(json: JSONObject): Int {
            return json.getInt(dataTypeJSON)
        }

        const val baseURL: String = "https://forums.redflagdeals.com"
        const val dealListURL: String = "/hot-deals-f9"
        const val searchFilterURL: String = "/?st=1&rfd_sk=tt&sd=d"
        const val categoryPrefixURL: String = "&c="
    }

    private val defaultURL =
        baseURL + dealListURL + searchFilterURL + categoryPrefixURL + category.toString()

    override suspend fun getAllPosts(): SortedPostList {
        return getPosts(defaultURL, 100)
    }

    override suspend fun getNewPosts(): SortedPostList {
        val posts = getPosts(defaultURL, 100).also {
            it.removeAllOlderThan(mostRecentPostDate)
        }
        if (posts.isNotEmpty()) {
            mostRecentPostDate = posts[0].date
        }
        return posts
    }

    override fun getName(): String {
        return "RFD, category = $category"
    }


    /**
     * Gets at least a certain amount of posts if available
     *
     * @param urlString The url in String format from which to get posts
     * @param number The number of posts to get at least
     */
    private suspend fun getPosts(urlString: String, number: Int): SortedPostList {
        val posts = SortedPostList()

        if (number > 0) {

            val doc: Document = Jsoup.parse(getData(urlString))
            val htmlPosts = doc.getElementsByClass("thread_info_title")


            for (htmlPost: Element in htmlPosts) {
                val post = createRfdPost(htmlPost)
                posts.add(post)
            }

            //Gets the url of the next page
            val nextTag = doc.getElementsByClass("pagination_next").firstOrNull()
            if (nextTag != null) {
                //Does recursive calls to go through all the pages
                posts.addAll(
                    getPosts(
                        baseURL + nextTag.attr("href"),
                        number - posts.size
                    )
                )
            }
        }

        return posts
    }

    @SuppressLint("SimpleDateFormat")
    private fun createRfdPost(htmlPost: Element): Post {
        val aTag = htmlPost.getElementsByTag("h3")[0].getElementsByTag("a").last()

        //TODO It seems that RFD switches from EST to EDT and back depending on daylight savings time, need to find out a way to deal with it
        //The regex is there for greatly simplifying date parsing, the EDT is there because afaik RFD uses EDT
        val dateString: String = htmlPost.getElementsByClass("first-post-time").text()
            .replace("(?<=\\d)(rd|st|nd|th)\\b,".toRegex(), "") + " EST"


        val id = aTag.attr("href")

        val simpleDateFormat = SimpleDateFormat("MMM d yyyy h:mm a z")

        return Post(
            title = aTag.text(),
            description = "",
            id = id,
            url = URL("https://forums.redflagdeals.com/$id"),
            date = simpleDateFormat.parse(dateString),
            source = "RedFlagDeals: Hot Deals"
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RFDScraper

        if (category != other.category) return false

        return true
    }

    override fun toJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(scraperTypeJSON, rfdScraperName)

        val dataJSONObject = JSONObject()
        dataJSONObject.put(dataTypeJSON, category)

        jsonObject.put(dataNameJSON, dataJSONObject)
        return jsonObject
    }

    override fun hashCode(): Int {
        return category
    }
}