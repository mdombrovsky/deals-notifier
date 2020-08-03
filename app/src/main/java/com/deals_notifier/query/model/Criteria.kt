package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post

class Criteria(val keywords: ArrayList<Keyword> = ArrayList<Keyword>()) {

    fun matches(post: Post): Boolean {
        for(keyword:Keyword in keywords){
            if(keyword.matches(post)){
                return true
            }
        }
        return false
    }


}