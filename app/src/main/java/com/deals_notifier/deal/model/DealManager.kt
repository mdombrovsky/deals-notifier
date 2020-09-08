import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder

interface DealManager{
    val posts: SortedPostList
    val queryHolder: QueryHolder
    fun reset()
    suspend fun updatePosts(): List<Post>
}