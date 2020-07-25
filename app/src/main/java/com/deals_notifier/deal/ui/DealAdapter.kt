package com.deals_notifier.deal.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.model.DealController
import com.deals_notifier.post.model.Post
import kotlinx.android.synthetic.main.deal_row.view.*

class DealAdapter(private val context: Context) :
    RecyclerView.Adapter<DealAdapter.DealHolder>() {

    lateinit var controller: DealController
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.deal_row, parent, false)
        return DealHolder(view)

    }

    override fun onBindViewHolder(holder: DealHolder, position: Int) {
        holder.title.text = controller.getTitle(position)
    }

    override fun getItemCount(): Int =
        if (this::controller.isInitialized) this.controller.getSize() else 0

    inner class DealHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.dealTitle
    }
}