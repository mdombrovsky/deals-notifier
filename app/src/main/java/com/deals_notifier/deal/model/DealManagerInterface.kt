import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.Scraper

interface DealManagerInterface {
    val posts: SortedPostList
    val queryHolder: QueryHolder
    val scrapers:MutableList<Scraper>
    fun reset()
    suspend fun updatePosts(): List<Post>
}