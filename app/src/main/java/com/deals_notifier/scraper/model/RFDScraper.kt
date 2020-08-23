package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.text.SimpleDateFormat

class RFDScraper(private val category: Int) : Scraper() {
    //Category: 0 -> All, 9 -> Computers & Electronics


    private companion object {
        const val baseURL: String = "https://forums.redflagdeals.com"
        const val dealListURL: String = "/hot-deals-f9"
        const val searchFilterURL: String = "/?st=1&rfd_sk=tt&sd=d"
        const val categoryPrefixURL: String = "&c="
    }

    private var mostRecentPostId: String? = null

    private val defaultURL =
        baseURL + dealListURL + searchFilterURL + categoryPrefixURL + category.toString()

    override suspend fun getAllPosts(): List<Post> {
        return getPosts(URL(defaultURL), 100)
    }

    override suspend fun getNewPosts(): List<Post> {
        val posts = getPosts(URL(defaultURL), 100, mostRecentPostId)
        if (posts.isNotEmpty()) {
            mostRecentPostId = posts[0].id
        }
        return posts
    }

    /**
     * Gets at least a certain amount of posts if available
     *
     * @param url The url from which to get posts
     * @param number The number of posts to get at least
     * @param id If set, this will stop searching after a post with matching id has been found
     */
    private suspend fun getPosts(url: URL, number: Int, id: String? = null): ArrayList<Post> {
        val posts = ArrayList<Post>()

        if (number > 0) {

            val doc: Document = Jsoup.parse(getData(url))
            val htmlPosts = doc.getElementsByClass("thread_info_title")


            for (htmlPost: Element in htmlPosts) {
                val post = createRfdPost(htmlPost)

                //Hopefully this will be optimized out at compile time
                //Better to write code that is much more readable than a tiny bit faster
                if (post.id == id) {
                    //If reached the id, we are done
                    return posts
                }
                posts.add(post)
            }

            //Gets the url of the next page
            val nextTag = doc.getElementsByClass("pagination_next").firstOrNull()
            if (nextTag != null) {
                //Does recursive calls to go through all the pages
                posts.addAll(
                    getPosts(
                        URL(baseURL + nextTag.attr("href")),
                        number - posts.size
                    )
                )
            }
        }

        return posts
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