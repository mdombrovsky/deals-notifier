package com.deals_notifier.deal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.controller.DealController
import kotlinx.android.synthetic.main.deal_card.view.*

class DealAdapter() :
    RecyclerView.Adapter<DealAdapter.ViewHolder>() {

    lateinit var controller: DealController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.deal_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =
        if (this::controller.isInitialized) this.controller.getSize() else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = controller.getTitle(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.dealTitle
    }
}