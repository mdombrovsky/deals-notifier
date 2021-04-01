package com.deals_notifier.query.model

import com.deals_notifier.post.model.Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject


class Query(
    var title: String = "",
    criteriaInput: ArrayList<Criteria> = ArrayList(),
    var enabled: Boolean = true
) :
    SearchComponent {
    constructor(json: JSONObject) : this(
        getTitleFromJSON(json),
        getCriteriaFromJSON(json),
        getEnabledFromJSON(json)
    )

    companion object {

        private const val titleJSONName = "title"
        private const val criteriaJSONName = "criteria"
        private const val enableJSONName = "enabled"

        private fun getTitleFromJSON(json: JSONObject): String {
            return json.getString(titleJSONName)
        }

        private fun getEnabledFromJSON(json: JSONObject): Boolean {
            return json.getBoolean(enableJSONName)
        }

        private fun getCriteriaFromJSON(json: JSONObject): ArrayList<Criteria> {
            val jsonCriteriaArray = json.getJSONArray(criteriaJSONName)
            val criteria = ArrayList<Criteria>()

            for (i in 0 until jsonCriteriaArray.length()) {
                criteria.add(Criteria(jsonCriteriaArray.getJSONObject(i)))
            }

            return criteria
        }

        // Parses string into query object
        fun getQueryFrom(contents: String): Query? {

            val query: Query
            var subStrings: List<String>
            var tempStr: String
            val criteriaList = ArrayList<Criteria>()
            var keywordList = ArrayList<Keyword>()
            var numBrackets = 0
            val ands = contents.split("&")
            for (s in ands) {

                if (s.contains("&")) {
                    return null
                }

                for (c in s) {
                    if (c == '(') {
                        numBrackets++
                    } else if (c == ')') {
                        numBrackets--
                    }
                }

                if (numBrackets != 0) {
                    return null
                }

                subStrings = s.split("|")

                for (subStr in subStrings) {
                    if (subStr.trim().contains("|") || subStr.trim().isEmpty()) {
                        return null
                    }

                    tempStr = subStr.trim().replace("(", "")
                    tempStr = tempStr.replace(")", "")

                    keywordList.add(Keyword(tempStr.trim()))
                }

                criteriaList.add(Criteria(keywordList))
                keywordList = ArrayList()

            }

            query = Query("", criteriaList)
            return query
        }
    }

    private val mutex: Mutex = Mutex()
    private val criteriaArrayList: ArrayList<Criteria> = criteriaInput
    val criteria: List<Criteria> = criteriaArrayList
    var queryDescription: String = regenDescription()
        private set


    private fun regenDescription(): String {

        val str = StringBuilder()

        for (c in criteria) {
            str.append(c.getCriteriaDescription())
            str.append(" & ")
        }

        return str.removeRange(str.length - 3, str.length - 1).toString()

    }

    override suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            if (!enabled) {
                return false
            }
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
        jsonQuery.put(enableJSONName, enabled)

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