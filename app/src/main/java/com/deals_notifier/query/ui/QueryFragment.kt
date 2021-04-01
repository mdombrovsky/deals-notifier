package com.deals_notifier.query.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.MainActivity
import com.deals_notifier.R
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.query.controller.QueryFragmentController
import com.deals_notifier.query.model.Query
import kotlinx.android.synthetic.main.fragment_query.view.*

class QueryFragment(val controller: QueryFragmentController) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_query, container, false)
        implementRecyclerView(view)
        return view

    }

    private fun implementRecyclerView(view: View) {

        val queryAdapter = controller.createQueryAdapter()

        view.addQueryButton.setOnClickListener {

            //Parses input text into query
            val query = Query.getQueryFrom(view.query_search_terms.text.toString())
            if (query == null) {
                // If query is invalid format, notify user
                Toast.makeText(
                    MainActivity.mainActivity,
                    "Invalid query terms entered",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // If query is valid format, allow user to enter title
                textInputModal(
                    context = view.context,
                    title = "Query Title",
                    onSuccess = { text: String -> queryAdapter.controller.add(text, query) }
                )
            }
            view.query_search_terms.setText("")
        }

        val recyclerView: RecyclerView = view.queryRecyclerView
        recyclerView.apply {
            adapter = queryAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


}