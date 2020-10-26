package com.deals_notifier.scraper.ui

import android.content.Context
import com.deals_notifier.input_modal.ui.buttonInputModal
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.scraper.model.RFDScraper
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.Scraper

object ScraperInputModal {

    fun launchModal(context: Context, onSuccess: (Scraper) -> Unit) {
        buttonInputModal(
            context = context,
            title = "New Scraper",
            items = arrayOf("Reddit", "RFD")
        ) {

            when (it) {
                0 -> {
                    textInputModal(
                        context = context,
                        title = "New Reddit Scraper",
                        message = "Enter subreddit"
                    ) { text ->
                        onSuccess(RedditScraper(text))
                    }
                }
                else -> {
                    textInputModal(
                        context = context,
                        title = "New RFD Scraper",
                        message = "Enter category number as seen at the end of the URL"
                    ) { text ->
                        onSuccess(RFDScraper(text.toInt()))
                    }
                }
            }
        }

    }

}