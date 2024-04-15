package com.example.currencyexchange.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.databinding.BalancesListItemBinding
import com.example.currencyexchange.ui.model.BalanceEntryUiModel

class BalanceEntriesAdapter(
    private var items: List<BalanceEntryUiModel>,
    val onItemClicked: (button: BalanceEntryUiModel) -> Unit,
) : RecyclerView.Adapter<BalanceEntriesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: BalancesListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BalancesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textAmount.text = items[position].balance
            textBaseAmount.text = items[position].baseBalance
            textCurrencyCode.text = items[position].currencyCode
        }
    }

    fun updateButtons(items: List<BalanceEntryUiModel>) {
        this.items = items
        notifyDataSetChanged() //TODO replace with DiffUtil
    }
}