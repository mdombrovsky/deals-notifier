package com.deals_notifier.deal.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.post.model.Post

class DealAdapter(private val context: Context, var deals: List<Post> = ArrayList()) :
    RecyclerView.Adapter<DealHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.deal_row, parent, false)
        return DealHolder(view)
    }

    override fun onBindViewHolder(holder: DealHolder, position: Int) {
        holder.title.text = deals[position].title
    }

    override fun getItemCount(): Int {
        return this.deals.size
    }
}