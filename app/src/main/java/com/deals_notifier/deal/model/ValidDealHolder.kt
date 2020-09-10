package com.deals_notifier.deal.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ValidDealHolder(
    val queryHolder: QueryHolder,
    private val scrapers: ArrayList<Scraper>,
    private val removeAfterMilliseconds: Long = 24 * 60 * 60 * 1000
) :
    Serializable {

    val posts: SortedPostList = SortedPostList()

    fun reset() {
        posts.reset()
        for (scraper: Scraper in scrapers) {
            scraper.reset()
        }
    }

    suspend fun updatePosts(): List<Post> {
        val newPosts = getValidPosts(getNewPosts())

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

    private suspend fun getPosts(): SortedPostList{
        val posts = SortedPostList()
        for (scraper: Scraper in scrapers) {
            posts.addAll(scraper.getAllPosts())
        }
        return posts
    }


    private suspend fun getNewPosts(): SortedPostList {
        val posts = SortedPostList()
        for (scraper: Scraper in scrapers) {
            posts.addAll(scraper.getNewPosts())
        }
        return posts
    }

    private fun getValidPosts(posts: List<Post>): SortedPostList {
        val validPosts = SortedPostList()
        for (post: Post in posts) {
            if (queryHolder.matches(post)) {
                validPosts.add(post)
            }
        }
        return validPosts
    }


}