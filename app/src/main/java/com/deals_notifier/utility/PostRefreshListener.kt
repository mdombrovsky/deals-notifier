package com.deals_notifier.utility

import com.deals_notifier.post.model.Post

interface PostRefreshListener : OnCompleteListener<List<Post>> {
}