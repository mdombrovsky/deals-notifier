package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import org.json.JSONArray
import org.json.JSONObject


class Query(var title: String = "", val criteria: ArrayList<Criteria> = ArrayList()) :
    SearchComponent {
    constructor(json: JSONObject) : this(getTitleFromJSON(json), getCriteriaFromJSON(json))

    private companion object {
        const val titleJSONName = "title"
        const val criteriaJSONName = "criteria"

        fun getTitleFromJSON(json: JSONObject): String {
            return json.getString(titleJSONName)
        }

        fun getCriteriaFromJSON(json: JSONObject): ArrayList<Criteria> {
            val jsonCriteriaArray = json.getJSONArray(criteriaJSONName)
            val criteria = ArrayList<Criteria>()

            for (i in 0 until jsonCriteriaArray.length()) {
                criteria.add(Criteria(jsonCriteriaArray.getJSONObject(i)))
            }

            return criteria
        }
    }

    override fun matches(post: Post): Boolean {
        for (criteria: Criteria in this.criteria) {
            if (!criteria.matches(post)) {
                return false
            }
        }
        return true

    }

    override fun toJSON(): JSONObject {
        val jsonQuery = JSONObject()

        jsonQuery.put(titleJSONName, title)

        val jsonCriteriaArray = JSONArray()

        for (criteria: Criteria in this.criteria) {
            jsonCriteriaArray.put(criteria.toJSON())
        }

        jsonQuery.put(criteriaJSONName, jsonCriteriaArray)

        return jsonQuery
    }
}