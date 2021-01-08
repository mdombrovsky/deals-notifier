package com.deals_notifier.scraper.model

import com.deals_notifier.post.model.SortedPostList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ScraperHolder(scrapersInput: ArrayList<Scraper> = arrayListOf()) {

    companion object {
        fun createDefaultScrapers(): List<Scraper> {
            return arrayListOf(RedditScraper("bapcsalescanada"), RFDScraper(0))
        }

    }

    private val scrapersArrayList: ArrayList<Scraper> = scrapersInput
    val scrapers: List<Scraper> = scrapersArrayList

    private val mutex: Mutex = Mutex()

    fun reset() {
        for (scraper: Scraper in scrapers) {
            scraper.reset()
        }
    }

    suspend fun getNewPosts(): SortedPostList {

        val getterMethods: java.util.ArrayList<suspend () -> SortedPostList> = arrayListOf()

        mutex.withLock {
            //This is spread out to minimize time that is spent iterating over scrapers
            for (scraper: Scraper in scrapers) {
                getterMethods.add(scraper::getNewPosts)
            }
        }

        val jobList: java.util.ArrayList<Job> = arrayListOf()
        val resultsList: java.util.ArrayList<SortedPostList> = arrayListOf()

        for (getterMethod in getterMethods) {
            val results = SortedPostList()
            resultsList.add(results)
            jobList.add(CoroutineScope(Dispatchers.IO).launch {
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

    suspend fun addScraper(scraper: Scraper): Boolean {
        mutex.withLock {
            return if (scrapersArrayList.contains(scraper)) {
                false
            } else {
                scrapersArrayList.add(scraper)
                true
            }
        }
    }

    suspend fun removeScraper(scraper: Scraper): Boolean {
        mutex.withLock {
            return scrapersArrayList.remove(scraper)
        }
    }

    suspend fun removeScraperAt(index: Int): Scraper {
        mutex.withLock { return scrapersArrayList.removeAt(index) }
    }


}