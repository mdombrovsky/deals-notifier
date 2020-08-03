package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import java.util.*

class Keyword(text: String) {

    var text: String = ""
        set(value) {
            field = value
            textNoSpacesLowerCase = value.replace("\\s".toRegex(), "").toLowerCase(Locale.ENGLISH)
        }

    private var textNoSpacesLowerCase: String = ""

    init {
        this.text = text
    }

    fun matches(post: Post): Boolean {
        return post.contains(textNoSpacesLowerCase)
    }

}