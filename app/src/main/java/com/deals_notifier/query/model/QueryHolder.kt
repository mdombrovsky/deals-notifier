package com.deals_notifier.query.model

import android.content.Context
import android.util.Log
import com.deals_notifier.main.controller.TabController
import com.deals_notifier.post.model.Post
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class QueryHolder(queriesInput: ArrayList<Query> = arrayListOf()) : SearchComponent {

    constructor(json: JSONObject) : this(getQueriesFromJSON(json))

    private val queriesArrayList: ArrayList<Query> = queriesInput
    val queries: List<Query> = queriesArrayList

    companion object {
        const val filename = "queries.json"

        const val queriesJSONName = "queries"

        fun getQueriesFromJSON(json: JSONObject): ArrayList<Query> {
            val jsonQueryArray = json.getJSONArray(queriesJSONName)
            val queries = ArrayList<Query>()

            for (i in 0 until jsonQueryArray.length()) {
                queries.add(Query(jsonQueryArray.getJSONObject(i)))
            }
            return queries
        }

        suspend fun load(context: Context): QueryHolder {
            val file = File(context.filesDir, filename)

            return try {
                QueryHolder(JSONObject(file.readText()))
            } catch (e: Exception) {
                Log.e(TabController::class.simpleName, "Error reading queries from file: $e")
                QueryHolder()
            }

        }

    }

    private val mutex: Mutex = Mutex()

    suspend fun save(context: Context) {
        val file = File(context.filesDir, filename)
        try {
            file.writeText(this.toJSON().toString(4))
            Log.d(
                this.javaClass.simpleName,
                "Successfully saved queries to file: ${file.toString()}"
            )
        } catch (e: java.lang.Exception) {
            Log.e(this.javaClass.simpleName, "Error writing queries to file: $e")
        }
    }

    override suspend fun matches(post: Post): Boolean {
        mutex.withLock {
            for (query: Query in queriesArrayList) {
                if (query.matches(post)) {
                    return true
                }
            }
            return false
        }
    }


    override fun toJSON(): JSONObject {
        val jsonQueryHolder = JSONObject()

        val jsonQueryArray = JSONArray()

        for (query: Query in queriesArrayList) {
            jsonQueryArray.put(query.toJSON())
        }

        jsonQueryHolder.put(queriesJSONName, jsonQueryArray)

        return jsonQueryHolder
    }

    suspend fun addQuery(query:Query): Boolean {
        mutex.withLock {
            return queriesArrayList.add(query)
        }
    }

    suspend fun removeQueryAt(index: Int): Query {
        mutex.withLock {
            return queriesArrayList.removeAt(index)
        }
    }

}