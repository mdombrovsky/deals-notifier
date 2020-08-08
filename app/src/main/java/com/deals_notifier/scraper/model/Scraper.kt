package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.Post
import java.io.Serializable

abstract class Scraper:Serializable {
    abstract suspend fun getPosts(): List<Post>
    abstract suspend fun getNewPosts(): List<Post>
}