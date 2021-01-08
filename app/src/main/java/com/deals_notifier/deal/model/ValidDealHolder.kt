package com.deals_notifier.deal.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.ScraperHolder
import java.io.Serializable
import java.util.*

class ValidDealHolder(
    val queryHolder: QueryHolder,
    val scraperHolder: ScraperHolder,
    private val removeAfterMilliseconds: Long = 24 * 60 * 60 * 1000
) :
    Serializable {

    val posts: SortedPostList = SortedPostList()

    fun reset() {
        posts.reset()
        scraperHolder.reset()
    }

    suspend fun updatePosts(): List<Post> {
        val newPosts = getValidPosts(scraperHolder.getNewPosts())

        val oldestAllowedDate = Date(System.currentTimeMillis() - removeAfterMilliseconds)

        //Some scrapers don't listen to my request for only posts over the past date
        //Cough*** Cough*** RFDScraper
        newPosts.removeAllOlderThan(oldestAllowedDate)

        //Eliminate all old posts that are currently stored
        posts.removeAllOlderThan(oldestAllowedDate)

        //Merge the two lists
        posts.addAll(newPosts)

        return newPosts
    }


    private suspend fun getValidPosts(posts: List<Post>): SortedPostList {
        val validPosts = SortedPostList()
        for (post: Post in posts) {
            if (queryHolder.matches(post)) {
                validPosts.add(post)
            }
        }
        return validPosts
    }


}