package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.query.model.KeywordController
import kotlinx.android.synthetic.main.individual_keyword.view.*

class KeywordAdapter: RecyclerView.Adapter<KeywordAdapter.ViewHolder>() {

    lateinit var controller: KeywordController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.individual_keyword, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (this::controller.isInitialized) this.controller.getSize() else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keyWord.text = controller.getKeyWord(position)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val keyWord:TextView = itemView.keyWord
    }

}