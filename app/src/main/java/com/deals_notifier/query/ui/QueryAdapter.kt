package com.deals_notifier.query.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.input_modal.ui.textInputModal
import com.deals_notifier.query.controller.QueryController
import kotlinx.android.synthetic.main.query_card.view.*

class QueryAdapter(val controller: QueryController) :
    RecyclerView.Adapter<QueryAdapter.ViewHolder>() {

    private var isCollapsed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.query_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.controller.getSize()
    }

    private fun setQueryVisibility(holder: ViewHolder) {
        holder.queryDescription.visibility = if (!isCollapsed) View.VISIBLE else View.GONE
        holder.closeQueryButton.visibility = if (!isCollapsed) View.VISIBLE else View.GONE
        holder.dropDownQueryButton.visibility = if (isCollapsed) View.VISIBLE else View.GONE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.queryTitle.text = controller.getQueryTitle(position)

        setQueryVisibility(holder)

        holder.editQueryTitle.setOnClickListener {
            textInputModal(
                context = holder.editQueryTitle.context,
                title = "Edit Query Title",
                onSuccess =
                { text: String -> controller.setQueryTitle(holder.adapterPosition, text) }
            )
        }

        holder.dropDownQueryButton.setOnClickListener {
            isCollapsed = false
            setQueryVisibility(holder)
        }

        holder.closeQueryButton.setOnClickListener {
            isCollapsed = true
            setQueryVisibility(holder)
        }

        holder.deleteQueryButton.setOnClickListener {
            controller.remove(holder.adapterPosition)
        }

        // sets the onOffSwitch on load
        holder.onOffSwitchQueryButton.isChecked = controller.isQueryEnabled(holder.adapterPosition)

        holder.onOffSwitchQueryButton.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            controller.setQueryEnabled(holder.adapterPosition, isChecked)
        }

        holder.queryDescription.text = controller.getQueryDescription(holder.adapterPosition)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val queryTitle: TextView = itemView.queryTitle
        val queryDescription: TextView = itemView.saved_query
        val editQueryTitle: ImageButton = itemView.editQueryTitleButton
        val deleteQueryButton: ImageButton = itemView.deleteQueryButton
        val closeQueryButton: ImageButton = itemView.close_query
        val dropDownQueryButton: ImageButton = itemView.dropdown_query
        val onOffSwitchQueryButton: SwitchCompat = itemView.queryOnOffSwitch
    }
}