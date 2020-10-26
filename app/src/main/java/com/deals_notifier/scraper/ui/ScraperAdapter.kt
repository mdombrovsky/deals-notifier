package com.deals_notifier.scraper.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.scraper.controller.ScraperController
import kotlinx.android.synthetic.main.scraper_card.view.*

class ScraperAdapter(val controller: ScraperController) :
    RecyclerView.Adapter<ScraperAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.scraper_card, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.scraperName.text = controller.getName(position)
        holder.deleteScraperButton.setOnClickListener{
            controller.remove(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = this.controller.getSize()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteScraperButton: ImageButton = itemView.deleteScraperButton
        val scraperName:TextView=itemView.scraperName
    }

}