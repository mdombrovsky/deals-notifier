package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.query.controller.CriteriaController
import kotlinx.android.synthetic.main.criteria_column.view.*

class CriteriaAdapter(val controller: CriteriaController) :
    RecyclerView.Adapter<CriteriaAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.criteria_column, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return this.controller.getSize()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val newAdapter = controller.createKeyWordAdapter(position)
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.VERTICAL, false)
            adapter = newAdapter

        }
        holder.recyclerView.setRecycledViewPool(viewPool)


        holder.addKeywordButton.setOnClickListener {
            textInputModal(
                context = holder.addKeywordButton.context,
                title = "Add Keyword",
                onSuccess = { text: String -> newAdapter.controller.add(text) }
            )
        }

        holder.deleteCriteriaButton.setOnClickListener {
            controller.remove(holder.adapterPosition)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.recyclerView
        val addKeywordButton: Button = itemView.addKeywordButton
        val deleteCriteriaButton: ImageButton = itemView.deleteColumnButton
    }
}