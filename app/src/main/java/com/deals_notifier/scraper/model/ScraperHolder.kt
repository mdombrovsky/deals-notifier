package com.deals_notifier.scraper.model

import android.content.Context
import android.util.Log
import com.deals_notifier.main.controller.TabController
import com.deals_notifier.post.model.SortedPostList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ScraperHolder(scrapersInput: ArrayList<Scraper> = arrayListOf()) {
    constructor(json: JSONObject) : this(getScrapersFromJSON(json))

    companion object {
        private const val scrapersJSONName = "scrapers"
        private const val filename = "scrapers.json"

        fun getScrapersFromJSON(json: JSONObject): ArrayList<Scraper> {
            val jsonArray = json.getJSONArray(scrapersJSONName)
            val scrapers = ArrayList<Scraper>()
            for (i in 0 until jsonArray.length()) {
                scrapers.add(Scraper.getScraperFromJSON(jsonArray.getJSONObject(i)))
            }
            return scrapers
        }

        fun createDefaultScrapers(): List<Scraper> {
            return arrayListOf(RedditScraper("bapcsalescanada"), RFDScraper(0))
        }

        suspend fun load(context: Context): ScraperHolder {
            val file = File(context.filesDir, filename)

            return try {
                ScraperHolder(JSONObject(file.readText()))
            } catch (e: Exception) {
                Log.e(TabController::class.simpleName, "Error reading scrapers from file: $e")
                ScraperHolder()
            }

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

    suspend fun save(context: Context) {
        val file = File(context.filesDir, filename)
        try {
            file.writeText(this.toJSON().toString(4))
            Log.d(
                this.javaClass.simpleName,
                "Successfully saved scrapers to file: ${file.toString()}"
            )
        } catch (e: java.lang.Exception) {
            Log.e(this.javaClass.simpleName, "Error writing scrapers to file: $e")
        }
    }

    private fun toJSON(): JSONObject {
        val jsonScraperHolder = JSONObject()
        val jsonArray = JSONArray()

        for (scraper in scrapers) {
            jsonArray.put(scraper.toJSON())
        }

        jsonScraperHolder.put(scrapersJSONName, jsonArray)

        return jsonScraperHolder
    }

}