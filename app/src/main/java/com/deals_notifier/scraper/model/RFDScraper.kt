package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class RFDScraper : Scraper() {
    private var mostRecentPostId: String? = null


    override suspend fun getPosts(): List<Post> {

        val url: URL = URL("https://forums.redflagdeals.com/hot-deals-f9/?st=1&rfd_sk=tt&sd=d&c=9")
        return rfdHtmlToPosts(getData(url))

    }

    override suspend fun getNewPosts(): List<Post> {
        TODO("Not yet implemented")
    }

    private fun rfdHtmlToPosts(html: String): ArrayList<Post> {
        val doc: Document = Jsoup.parse(html)
        val htmlPosts = doc.getElementsByClass("thread_info_title")

        val posts = ArrayList<Post>()
        for (htmlPost: Element in htmlPosts) {
            posts.add(createRfdPost(htmlPost))
        }
        return posts
    }

    private fun createRfdPost(htmlPost: Element): Post {
        val aTag = htmlPost.getElementsByTag("h3")[0].getElementsByTag("a").last()

        //The regex is there for greatly simplifying date parsing, the EDT is there because afaik RFD uses EDT
        val dateString: String = htmlPost.getElementsByClass("first-post-time").text()
            .replace("(?<=\\d)(rd|st|nd|th)\\b,".toRegex(), "") + " EDT"

        val id = aTag.attr("href")

        val simpleDateFormat = SimpleDateFormat("MMM d yyyy H:mm a z")

        return Post(
            title = aTag.text(),
            description = "",
            id = id,
            url = URL("https://forums.redflagdeals.com/$id"),
            date = simpleDateFormat.parse(dateString)
        )
    }
}