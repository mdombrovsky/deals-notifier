package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post


class Query(var title: String = "", val criteria: ArrayList<Criteria> = ArrayList()) {

    fun matches(post: Post): Boolean {
        for(criteria:Criteria in this.criteria){
            if(!criteria.matches(post)){
                return false
            }
        }
        return true

    }
}