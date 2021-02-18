package com.deals_notifier.deal.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.MainActivity
import com.deals_notifier.R
import com.deals_notifier.deal.controller.DealController
import kotlinx.android.synthetic.main.deal_card.view.*


class DealAdapter(val controller: DealController) :
    RecyclerView.Adapter<DealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.deal_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = this.controller.getSize()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = controller.getTitle(position)
        holder.title.setOnClickListener {
            val url = controller.getURL(position)
            if (url != null) {
                MainActivity.mainActivity!!.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url.toString())
                    )
                )
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.dealTitle
    }
}