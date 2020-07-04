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

    object static {
        fun refresh() {
            val scraper: Scraper = RedditScraper("askreddit")

            CoroutineScope(IO).launch {

//                Log.d("Posts","1Loading")
                var posts: List<Post> = scraper.getNewPosts()
//                Log.d("Posts","1Size: "+posts.size.toString())
//                for(i in posts.indices){
//                    Log.d("Posts", (i+1).toString()+": "+ posts[i].title)
//                }
//                delay(10000)
//                Log.d("Posts","2Loading")
//                posts=scraper.getNewPosts()
//                Log.d("Posts","2Size: "+posts.size.toString())
//                for(i in posts.indices){
//                    Log.d("Posts", (i+1).toString()+": "+ posts[i].title)
//                }
//                Log.d("Posts","3Loading")
//                posts=scraper.getPosts()
//                Log.d("Posts","3Size: "+posts.size.toString())
//                for(i in posts.indices){
//                    Log.d("Posts", (i+1).toString()+": "+posts[i].title)
//                }
            }
        }
    }

}