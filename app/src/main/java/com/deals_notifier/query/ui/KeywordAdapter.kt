package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.query.controller.KeywordController
import kotlinx.android.synthetic.main.individual_keyword.view.*


class KeywordAdapter(val controller: KeywordController) :
    RecyclerView.Adapter<KeywordAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.individual_keyword, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.controller.getSize()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keyWord.text = controller.getKeyWord(position)
        holder.deleteKeywordButton.setOnClickListener {
            controller.remove(holder.adapterPosition)
        }

        holder.editKeywordButton.setOnClickListener(
            textInputModal(
                context = holder.editKeywordButton.context,
                title = "Edit Keyword",
                defaultValue = holder.keyWord.text.toString(),
                onSuccess = { text: String -> controller.edit(holder.adapterPosition, text) }
            )
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyWord: TextView = itemView.keyWord
        val editKeywordButton: ImageButton = itemView.editKeywordButton
        val deleteKeywordButton: ImageButton = itemView.deleteKeywordButton
    }

}