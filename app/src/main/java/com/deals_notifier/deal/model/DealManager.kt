package com.deals_notifier.deal.model

import DealManagerInterface
import android.util.Log
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.ScraperHolder
import com.deals_notifier.utility.PostRefreshListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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

        private var isRefreshing: Boolean = false
        private val refreshListeners: ArrayList<PostRefreshListener> = arrayListOf()

        private val mutex: Mutex = Mutex()


    }

    override val posts: SortedPostList
        get() = validDealHolder.posts

    override val queryHolder: QueryHolder
        get() = validDealHolder.queryHolder

    override val scraperHolder: ScraperHolder
        get() = validDealHolder.scraperHolder


    override fun reset() = validDealHolder.reset()


    /**
     * This will update data,
     * While running, it will share new data with all subsequent function calls
     */
    override fun updatePosts(listener: PostRefreshListener) {

        //Uses Coroutine because of mutex
        CoroutineScope(Dispatchers.IO).launch {

            //Critical section
            mutex.withLock {
                Log.d(this.javaClass.simpleName, "Entering Critical Section 1")


                //Registers listener regardless, so that if already running we can re-use those results
                refreshListeners.add(listener)

                //Only starts refresh if its not already running
                if (!isRefreshing) {
                    isRefreshing = true

                    CoroutineScope(IO).launch {

                        //Network call - updates data
                        val posts = validDealHolder.updatePosts()

                        //Second critical section
                        mutex.withLock {
                            Log.d(this.javaClass.simpleName, "Entering Critical Section 2")


                            //Sends current data to all listeners
                            for (refreshListener in refreshListeners) {
                                CoroutineScope(Main).launch {
                                    refreshListener.onComplete(posts)
                                }
                            }

                            //Clears already called listeners
                            refreshListeners.clear()

                            //Ready for next refresh now
                            isRefreshing = false

                            Log.d(this.javaClass.simpleName, "Exiting Critical Section 2")

                        }
                    }
                }
                Log.d(this.javaClass.simpleName, "Exiting Critical Section 1")

            }

        }
    }
}