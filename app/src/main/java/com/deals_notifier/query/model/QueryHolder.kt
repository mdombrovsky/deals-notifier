package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post

class QueryHolder {
    val queries = ArrayList<Query>()

    fun matches(post: Post): Boolean {
        for (query: Query in queries) {
            if (query.matches(post)) {
                return true
            }
        }
        return false
    }
}