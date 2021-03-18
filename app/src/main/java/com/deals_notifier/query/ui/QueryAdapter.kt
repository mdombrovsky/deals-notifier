package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.query.controller.QueryController
import kotlinx.android.synthetic.main.query_card.view.*

class QueryAdapter(val controller: QueryController) :
    RecyclerView.Adapter<QueryAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.query_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.controller.getSize()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newAdapter = controller.createCriteriaAdapter(position)
        holder.queryTitle.text = controller.getQueryTitle(position)
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = newAdapter
        }

        holder.recyclerView.setRecycledViewPool(viewPool)

        holder.addCriteriaButton.setOnClickListener {
            newAdapter.controller.add()
        }

        holder.editQueryTitle.setOnClickListener {
            textInputModal(
                context = holder.editQueryTitle.context,
                title = "Edit Query Title",
                onSuccess =
                { text: String -> controller.setQueryTitle(holder.adapterPosition, text) }
            )
        }

        holder.deleteQueryButton.setOnClickListener {
            controller.remove(holder.adapterPosition)
        }

        // sets the onOffSwitch on load
        holder.onOffSwitchQueryButton.isChecked = controller.isQueryEnabled(holder.adapterPosition)

        holder.onOffSwitchQueryButton.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            controller.setQueryEnabled(holder.adapterPosition, isChecked)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val queryTitle: TextView = itemView.queryTitle
        val recyclerView: RecyclerView = itemView.searchColumnRecyclerView
        val editQueryTitle: ImageButton = itemView.editQueryTitleButton
        val addCriteriaButton: Button = itemView.addColumnButton
        val deleteQueryButton: ImageButton = itemView.deleteQueryButton
        val onOffSwitchQueryButton: SwitchCompat = itemView.queryOnOffSwitch
    }
}