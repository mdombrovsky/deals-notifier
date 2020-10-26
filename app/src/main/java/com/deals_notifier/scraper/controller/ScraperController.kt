package com.deals_notifier.scraper.controller

import android.util.Log
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.scraper.model.RFDScraper
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.Scraper
import com.deals_notifier.scraper.ui.ScraperAdapter

class ScraperController {

    companion object {
        fun createDefaultScrapers(): List<Scraper> {
            return arrayListOf(RedditScraper("bapcsalescanada"), RFDScraper(0))
        }

    }

    val scraperAdapter: ScraperAdapter = ScraperAdapter(this)

    private val scrapers: MutableList<Scraper> = DealManager.instance!!.scrapers

    fun getSize(): Int {
        return scrapers.size
    }

    fun getName(position: Int): String {
        return scrapers[position].getName()
    }

    fun remove(position: Int) {
        if (position != -1) {
            scrapers.removeAt(position)
            scraperAdapter.notifyItemRemoved(position)
            DealManager.instance!!.reset()

        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }

    fun add(scraper: Scraper) {
        //Avoid duplicates
        //TODO add a toast or alert if it is a duplicate
        if (!scrapers.contains(scraper)) {
            scrapers.add(scraper)
            scraperAdapter.notifyItemInserted(scrapers.lastIndex)
            DealManager.instance!!.reset()
        }
    }


    fun resetToDefault() {
        scrapers.clear()
        scrapers.addAll(createDefaultScrapers())
        scraperAdapter.notifyDataSetChanged()
        DealManager.instance!!.reset()
    }

}