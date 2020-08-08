package com.deals_notifier.deal.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper
import java.io.Serializable

class ValidDealHolder(val queryHolder: QueryHolder, val scrapers: ArrayList<Scraper>) :
    Serializable {

    var posts: ArrayList<Post> = ArrayList()

    suspend fun updatePosts(): ValidDealHolder {
        posts = getValidPosts(getPosts())
        return this
    }

    private suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        for (scraper: Scraper in scrapers) {
            posts.addAll(scraper.getPosts())
        }
        return posts
    }

    private fun getValidPosts(posts: ArrayList<Post>): ArrayList<Post> {
        val validPosts = ArrayList<Post>()
        for (post: Post in posts) {
            if (queryHolder.matches(post)) {
                validPosts.add(post)
            }
        }
        return validPosts
    }


}