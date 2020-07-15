package com.deals_notifier.deal.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.post.model.Post
import kotlinx.android.synthetic.main.deal_row.view.*
import org.w3c.dom.Text

class DealHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val title:TextView = itemView.findViewById(R.id.dealTitle)
}