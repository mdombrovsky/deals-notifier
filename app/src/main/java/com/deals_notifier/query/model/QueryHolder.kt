package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import org.json.JSONArray
import org.json.JSONObject

class QueryHolder(val queries: ArrayList<Query> = ArrayList<Query>()) : SearchComponent {
    constructor(json: JSONObject) : this(getQueriesFromJSON(json))


    private companion object {
        const val queriesJSONName = "queries"

        fun getQueriesFromJSON(json: JSONObject): ArrayList<Query> {
            val jsonQueryArray = json.getJSONArray(queriesJSONName)
            val queries = ArrayList<Query>()

            for (i in 0 until jsonQueryArray.length()) {
                queries.add(Query(jsonQueryArray.getJSONObject(i)))
            }
            return queries
        }
    }

    override fun matches(post: Post): Boolean {
        for (query: Query in queries) {
            if (query.matches(post)) {
                return true
            }
        }
        return false
    }

    override fun toJSON(): JSONObject {
        val jsonQueryHolder = JSONObject()

        val jsonQueryArray = JSONArray()

        for (query: Query in queries) {
            jsonQueryArray.put(query.toJSON())
        }

        jsonQueryHolder.put(queriesJSONName, jsonQueryArray)

        return jsonQueryHolder
    }


}