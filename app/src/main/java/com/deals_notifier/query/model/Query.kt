package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject


class Query(
    var title: String = "",
    criteriaInput: ArrayList<Criteria> = ArrayList()
) :
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

    private val mutex: Mutex = Mutex()

    private val criteriaArrayList: ArrayList<Criteria> = criteriaInput
    val criteria: List<Criteria> = criteriaArrayList

    override suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            for (criteria: Criteria in this.criteriaArrayList) {
                if (!criteria.matches(post)) {
                    return false
                }
            }
            return true
        }

    }

    override fun toJSON(): JSONObject {
        val jsonQuery = JSONObject()

        jsonQuery.put(titleJSONName, title)

        val jsonCriteriaArray = JSONArray()

        for (criteria: Criteria in this.criteriaArrayList) {
            jsonCriteriaArray.put(criteria.toJSON())
        }

        jsonQuery.put(criteriaJSONName, jsonCriteriaArray)

        return jsonQuery
    }


    suspend fun addCriteria(criteria: Criteria): Boolean {
        mutex.withLock {
            return criteriaArrayList.add(criteria)
        }
    }

    suspend fun removeCriteriaAt(index: Int): Criteria {
        mutex.withLock {
            return criteriaArrayList.removeAt(index)
        }
    }
}