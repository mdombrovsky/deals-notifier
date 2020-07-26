package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.query.model.CriteriaController
import kotlinx.android.synthetic.main.criteria_column.view.*

class CriteriaAdapter() : RecyclerView.Adapter<CriteriaAdapter.ViewHolder>() {

    lateinit var controller: CriteriaController

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.criteria_column, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (this::controller.isInitialized) this.controller.getSize() else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.VERTICAL, false)
            adapter = controller.createKeyWordAdapter(position)

        }
        holder.recyclerView.setRecycledViewPool(viewPool)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.recyclerView
    }
}