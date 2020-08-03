package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.deal.input_modal.ui.textInputModal
import com.deals_notifier.query.model.KeywordController
import kotlinx.android.synthetic.main.individual_keyword.view.*


class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.KeywordHolder>() {

    lateinit var controller: KeywordController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.individual_keyword, parent, false)
        return KeywordHolder(view)
    }

    override fun getItemCount(): Int {
        return if (this::controller.isInitialized) this.controller.getSize() else 0
    }

    override fun onBindViewHolder(holder: KeywordHolder, position: Int) {
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

    inner class KeywordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyWord: TextView = itemView.keyWord
        val editKeywordButton: ImageButton = itemView.editKeywordButton
        val deleteKeywordButton: ImageButton = itemView.deleteKeywordButton
    }

}