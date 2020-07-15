package com.deals_notifier.admin

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.Scraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Admin(private val context: Context, private val dealRecyclerView: RecyclerView) {
    private val scraper: Scraper = RedditScraper("bapcsalescanada")
    lateinit var posts: List<Post>
    private val dealAdapter: DealAdapter = DealAdapter(context = context)


    init {

        dealRecyclerView.adapter = dealAdapter
        dealRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    fun refresh() {


        CoroutineScope(IO).launch {
            replaceDeals(scraper.getPosts())

        }

    }

    private suspend fun replaceDeals(deals: List<Post>) {
        withContext(Main) {
            dealAdapter.deals = deals
            dealAdapter.notifyDataSetChanged()
        }
    }

}