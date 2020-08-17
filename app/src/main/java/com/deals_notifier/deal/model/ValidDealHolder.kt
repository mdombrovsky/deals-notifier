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


    suspend fun updatePosts(): ValidDealHolder {
        posts.addAll(getValidPosts(getNewPosts()))
        posts.removeAllOlderThan(Date(System.currentTimeMillis() - removeAfterMilliseconds))
        return this
    }

    private suspend fun getPosts(): List<Post> {
        val posts = ArrayList<Post>()
        for (scraper: Scraper in scrapers) {
            posts.addAll(scraper.getPosts())
        }
        return posts
    }


    private suspend fun getNewPosts(): List<Post> {
        val posts = ArrayList<Post>()
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