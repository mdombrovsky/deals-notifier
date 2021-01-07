import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper
import com.deals_notifier.utility.PostRefreshListener

interface DealManagerInterface {
    val posts: SortedPostList
    val queryHolder: QueryHolder
    val scrapers: MutableList<Scraper>
    fun reset()

    /**
     * Updates posts
     *
     * @param listener this will execute when the update has concluded and will pass in new posts to it
     */
    fun updatePosts(listener: PostRefreshListener)

}