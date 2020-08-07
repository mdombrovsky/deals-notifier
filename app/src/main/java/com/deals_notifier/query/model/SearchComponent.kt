package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import org.json.JSONObject
import java.io.Serializable

interface SearchComponent : Serializable {

    /**
     * To convert the object to JSON
     */
    fun toJSON(): JSONObject

    /**
     * To see if a post matches the desired search
     */
    fun matches(post: Post): Boolean
}