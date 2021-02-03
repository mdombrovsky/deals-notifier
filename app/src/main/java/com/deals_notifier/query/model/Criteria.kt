package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject

class Criteria(keywordsInput: ArrayList<Keyword> = ArrayList<Keyword>()) : SearchComponent {
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

    private val keywordsArrayList: ArrayList<Keyword> = keywordsInput
    val keywords: List<Keyword> = keywordsArrayList

    private val mutex: Mutex = Mutex()

    override suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            for (keyword: Keyword in keywordsArrayList) {
                if (keyword.matches(post)) {
                    return true
                }
            }
        }
        return false
    }

    override fun toJSON(): JSONObject {
        val jsonCriteria = JSONObject()

        val jsonKeywordArray = JSONArray()
        for (keyword: Keyword in keywordsArrayList) {
            jsonKeywordArray.put(keyword.toJSON())
        }
        jsonCriteria.put(keywordsJSONName, jsonKeywordArray)

        return jsonCriteria
    }

    suspend fun addKeyword(keyword: Keyword): Boolean {
        mutex.withLock {
            return keywordsArrayList.add(keyword)
        }
    }

    suspend fun removeKeywordAt(index: Int): Keyword {
        mutex.withLock {
            return keywordsArrayList.removeAt(index)
        }
    }

}