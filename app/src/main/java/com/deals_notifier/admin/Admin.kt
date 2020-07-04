package com.deals_notifier.admin

import android.util.Log
import com.deals_notifier.post.model.Post
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.Scraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Admin() {
    val scraper: Scraper = RedditScraper("bapcsalescanada")

    fun refresh() {


        CoroutineScope(IO).launch {

            var posts: List<Post> = scraper.getNewPosts()
            Log.d("Posts", "1Size: " + posts.size.toString())
            for (i in posts.indices) {
                Log.d("Posts", (i + 1).toString() + ": " + posts[i].title)
            }


        }
    }

}