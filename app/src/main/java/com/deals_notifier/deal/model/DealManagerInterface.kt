package com.deals_notifier.deal.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.ScraperHolder
import com.deals_notifier.utility.PostRefreshListener

interface DealManagerInterface {
    val posts: List<Post>
    val queryHolder: QueryHolder
    val scraperHolder: ScraperHolder

    /**
     * Resets the state of the scrapers and posts to default
     */
    fun reset()

    /**
     * Updates posts
     *
     * @param listener this will execute when the update has concluded and will pass in new posts to it
     */
    fun updatePosts(listener: PostRefreshListener)

}