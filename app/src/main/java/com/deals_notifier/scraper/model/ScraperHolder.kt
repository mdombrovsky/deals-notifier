package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post

class ScraperHolder {
    val scrapers: ArrayList<Scraper> = ArrayList()

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        for (scraper: Scraper in scrapers) {
            posts.addAll(scraper.getPosts())
        }
        return posts
    }

}