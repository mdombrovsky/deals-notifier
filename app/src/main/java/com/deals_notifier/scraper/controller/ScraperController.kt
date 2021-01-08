package com.deals_notifier.scraper.controller

import android.util.Log
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.scraper.model.Scraper
import com.deals_notifier.scraper.model.ScraperHolder.Companion.createDefaultScrapers
import com.deals_notifier.scraper.ui.ScraperAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScraperController(private val onModified: () -> Unit) {


    val scraperAdapter: ScraperAdapter = ScraperAdapter(this)

    private val scrapers: List<Scraper> = DealManager.instance!!.scraperHolder.scrapers

    fun getSize(): Int {
        return scrapers.size
    }

    fun getName(position: Int): String {
        return scrapers[position].getName()
    }

    fun remove(position: Int) {
        if (position != -1) {
            CoroutineScope(IO).launch {
                DealManager.instance!!.scraperHolder.removeScraperAt(position)
                withContext(Main) {
                    scraperAdapter.notifyItemRemoved(position)
                    onModified()
                }
            }
        } else {
            Log.e(
                this.javaClass.simpleName,
                "Requesting to remove item that is not present"
            )
        }
    }

    fun add(scraper: Scraper) {
        //TODO add a toast or alert if it is a duplicate

        CoroutineScope(IO).launch {
            val success: Boolean = DealManager.instance!!.scraperHolder.addScraper(scraper)
            if (success) {
                withContext(Main) {
                    scraperAdapter.notifyItemInserted(scrapers.lastIndex)
                    onModified()
                }
            }
        }
    }


    fun resetToDefault() {
        //Only do stuff if different than default
        if (scrapers != createDefaultScrapers()) {
            CoroutineScope(IO).launch {

                for (scraper in scrapers) {
                    DealManager.instance!!.scraperHolder.removeScraper(scraper)
                }

                for (scraper in createDefaultScrapers()) {
                    DealManager.instance!!.scraperHolder.addScraper(scraper)
                }

                withContext(Main) {
                    scraperAdapter.notifyDataSetChanged()
                    onModified()
                }
            }
        }
    }

}