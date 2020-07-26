package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.query.model.QueryController
import kotlinx.android.synthetic.main.query_card.view.*

class QueryAdapter() :
    RecyclerView.Adapter<QueryAdapter.QueryHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    lateinit var controller: QueryController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.query_card, parent, false)
        return QueryHolder(view)
    }

    override fun getItemCount(): Int {
        return if (this::controller.isInitialized) this.controller.getSize() else 0
    }

    override fun onBindViewHolder(holder: QueryHolder, position: Int) {
        holder.title.text = controller.getQueryTitle(position)
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = controller.createCriteriaAdapter(position)
        }

        holder.recyclerView.setRecycledViewPool(viewPool)
    }

    inner class QueryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.queryTitle
        val recyclerView: RecyclerView = itemView.searchColumnRecyclerView
    }
}