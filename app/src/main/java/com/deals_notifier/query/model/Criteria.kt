package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import org.json.JSONArray
import org.json.JSONObject

class Criteria(val keywords: ArrayList<Keyword> = ArrayList<Keyword>()) {
    constructor(json: JSONObject) : this(getKeywordsFromJSON(json))

    private companion object {
        const val keywordsJSONName = "keywords"

        fun getKeywordsFromJSON(json: JSONObject): ArrayList<Keyword> {
            val jsonKeywordArray = json.getJSONArray(keywordsJSONName)

            val keywords = ArrayList<Keyword>()

            for (i in 0 until jsonKeywordArray.length()) {
                keywords.add(Keyword(jsonKeywordArray.getJSONObject(i)))
            }

            return keywords
        }
    }

    fun matches(post: Post): Boolean {
        for (keyword: Keyword in keywords) {
            if (keyword.matches(post)) {
                return true
            }
        }
        return false
    }

    fun toJSON(): JSONObject {
        val jsonCriteria = JSONObject()

        val jsonKeywordArray = JSONArray()
        for (keyword: Keyword in keywords) {
            jsonKeywordArray.put(keyword.toJSON())
        }
        jsonCriteria.put(keywordsJSONName, jsonKeywordArray)

        return jsonCriteria
    }

}