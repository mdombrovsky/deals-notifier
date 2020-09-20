import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder

interface DealManagerInterface {
    val posts: SortedPostList
    val queryHolder: QueryHolder
    fun reset()
    suspend fun updatePosts(triggerUpdate:Boolean = false): List<Post>
    fun addOnUpdateListener()
    fun removeOnUpdateListener()
}