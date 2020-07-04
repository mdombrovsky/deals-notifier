package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post

abstract class Scraper {
    abstract suspend fun getPosts(): List<Post>
    abstract suspend fun getNewPosts(): List<Post>
}