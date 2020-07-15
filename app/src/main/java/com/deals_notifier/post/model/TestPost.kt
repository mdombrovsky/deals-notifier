package com.deals_notifier.post.model

import java.net.URL

class TestPost(override val title: String, override val description: String) : Post() {
    override val id: String = "test.id"
    override val url: URL = URL("https://github.com/")
    override val stringToSearchNoSpaces: String = title + description
}