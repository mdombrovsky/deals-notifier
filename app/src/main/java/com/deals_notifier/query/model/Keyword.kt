package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import org.json.JSONObject
import java.util.*

class Keyword(text: String = "") : SearchComponent {
    constructor(json: JSONObject) : this(getKeywordFromJSON(json))

    private companion object {
        const val keywordJSONName = "keyword"

        fun getKeywordFromJSON(json: JSONObject): String {
            return json.getString(keywordJSONName)
        }
    }


    var text: String = ""
        set(value) {
            field = value
            textNoSpacesLowerCase = value.replace("\\s+".toRegex(), "")
                .toLowerCase(Locale.ENGLISH)
        }

    private var textNoSpacesLowerCase: String = ""

    init {
        this.text = text
    }

    override fun matches(post: Post): Boolean {
        return post.contains(textNoSpacesLowerCase)
    }

    override fun toJSON(): JSONObject {
        val jsonKeyword = JSONObject()

        jsonKeyword.put(keywordJSONName, text)

        return jsonKeyword
    }

}