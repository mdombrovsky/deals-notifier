package com.deals_notifier.deal.model

import DealManagerInterface
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper

class DealManager private constructor(private val validDealHolder: ValidDealHolder) :
    DealManagerInterface {
    companion object {

        fun initialize(validDealHolder: ValidDealHolder) {
            if (this.instance != null) {
                throw UnsupportedOperationException()
            }
            this.instance = DealManager(validDealHolder)
        }

        var instance: DealManagerInterface? = null
            private set
    }

    override val posts: SortedPostList
        get() = validDealHolder.posts

    override val queryHolder: QueryHolder
        get() = validDealHolder.queryHolder

    override val scrapers: MutableList<Scraper>
        get() = validDealHolder.scrapers

    override fun reset() = validDealHolder.reset()

    override suspend fun updatePosts(triggerUpdate: Boolean): List<Post> {
        return validDealHolder.updatePosts()
    }

    override fun addOnUpdateListener() {
        TODO("Not yet implemented")
    }

    override fun removeOnUpdateListener() {
        TODO("Not yet implemented")
    }

}