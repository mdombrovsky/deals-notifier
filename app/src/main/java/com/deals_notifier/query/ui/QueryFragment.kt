package com.deals_notifier.query.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.input_modal.ui.textInputModal
import com.deals_notifier.query.model.Criteria
import com.deals_notifier.query.model.Keyword
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryController
import kotlinx.android.synthetic.main.fragment_query.view.*

class QueryFragment() : Fragment() {

    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_query, container, false)

        addButton = view.addQueryButton

        implementRecyclerView(view)


        return view

    }

    private fun implementRecyclerView(view: View) {

        val queryAdapter = QueryAdapter()


        val queryController = QueryController(queryAdapter, createTestData())

        addButton.setOnClickListener(
            textInputModal(view.context, "Create Query")
            { text: String -> queryController.add(text) }
        )

        val recyclerView: RecyclerView = view.queryRecyclerView
        recyclerView.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun createTestData(): ArrayList<Query> {
        val keyword = Keyword("test 1")
        val keyword2 = Keyword("test 2")
        val keyword3 = Keyword("test 3")
        val criteria = Criteria()
        criteria.keywords = arrayListOf(keyword, keyword2)
        val criteria2 = Criteria()
        criteria2.keywords = arrayListOf(keyword3)
        val query = Query()
        query.title = "query1"
        query.criteria = arrayListOf(criteria, criteria2)
        val query2 = Query()
        query2.title = "query2"

        query2.criteria = arrayListOf(criteria2)

        val queries = arrayListOf(query, query2)

        return queries
    }
}