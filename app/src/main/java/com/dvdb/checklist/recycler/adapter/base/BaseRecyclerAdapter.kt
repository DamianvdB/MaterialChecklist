package com.dvdb.checklist.recycler.adapter.base

import androidx.recyclerview.widget.RecyclerView

@Suppress("PropertyName")
internal abstract class BaseRecyclerAdapter<T>(
    items: List<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var _items: MutableList<T> = items.toMutableList()

    override fun getItemCount() = _items.size

    var items: List<T>
        get() = _items.toList()
        set(value) {
            _items = value.toMutableList()
            notifyDataSetChanged()
        }

    fun addItem(item: T, position: Int, notify: Boolean = true) {
        _items.add(position, item)
        if (notify) {
            notifyItemInserted(position)
        }
    }

    fun removeItem(position: Int, notify: Boolean = true) {
        _items.removeAt(position)
        if (notify) {
            notifyItemRemoved(position)
        }
    }

    fun updateItem(item: T, position: Int, notify: Boolean = true) {
        _items[position] = item
        if (notify) {
            notifyItemChanged(position)
        }
    }
}