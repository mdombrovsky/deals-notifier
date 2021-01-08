package com.deals_notifier.deal.model

import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

class ValidDealHolder(
    val queryHolder: QueryHolder,
    val scrapers: ArrayList<Scraper>,
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


    private suspend fun getNewPosts(): SortedPostList {

        val getterMethods: ArrayList<suspend () -> SortedPostList> = arrayListOf()

        //This is spread out to minimize time that is spent iterating over scrapers
        for (scraper: Scraper in scrapers) {
            getterMethods.add(scraper::getNewPosts)
        }

        val jobList: ArrayList<Job> = arrayListOf()
        val resultsList: ArrayList<SortedPostList> = arrayListOf()

        for (getterMethod in getterMethods) {
            val results = SortedPostList()
            resultsList.add(results)
            jobList.add(CoroutineScope(IO).launch {
                results.addAll(getterMethod.invoke())
            })
        }

        jobList.forEach {
            it.join()
        }

        val posts = SortedPostList()

        resultsList.forEach {
            posts.addAll(it)
        }

        return posts
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