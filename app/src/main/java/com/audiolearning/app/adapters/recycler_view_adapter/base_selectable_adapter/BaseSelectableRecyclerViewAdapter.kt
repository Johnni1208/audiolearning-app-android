package com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.audiolearning.app.adapters.AdapterDataEvent
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.BaseSelectableRecyclerViewAdapter.BaseSelectableViewHolder
import com.audiolearning.app.data.db.entities.BaseEntity

/**
 * Base RecyclerView with selecting functionality. Has functions for initializing
 * data ([initializeData]) and updating data ([updateData]).
 * Use a child of [BaseSelectableViewHolder] as Adapter.
 */
@Suppress("LeakingThis")
abstract class BaseSelectableRecyclerViewAdapter<T : BaseEntity> :
    RecyclerView.Adapter<BaseSelectableRecyclerViewAdapter<T>.BaseSelectableViewHolder>() {
    var isDataInitialized = false
    var isSelecting = false

    protected var data: ArrayList<T> = arrayListOf()

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseSelectableViewHolder

    abstract override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int)

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position

    /**
     * Sets the initial data for this adapter. For more modifications use [updateData]
     *
     * @param data Initial data
     *
     * @throws IllegalStateException If the adapter's data has already been initialized
     */
    fun initializeData(data: List<T>) {
        check(!isDataInitialized) { "Already initialized. Use updateData()" }

        this.data = ArrayList(data)
        isDataInitialized = true
        notifyDataSetChanged()
    }

    /**
     * Use this function to update the data of the adapter (remove/add)
     *
     * @param newData List of new BaseEntities
     *
     * @return [AdapterDataEvent] Whether it deleted or added an item
     *
     * @throws IllegalStateException If the data has not yet been initialized.
     */
    fun updateData(newData: List<T>): AdapterDataEvent {
        check(isDataInitialized) { "Not yet initialized. Use initializeData()" }

        // Adds items
        if (newData.size > data.size) {
            for (i in newData.lastIndex downTo 0) {
                if (!data.contains(newData[i])) addItem(newData[i])
            }

            return AdapterDataEvent.ITEMS_ADDED
        }

        // Removes items
        val positionsOfDeletedItems: ArrayList<Int> = arrayListOf()
        for (i in 0 until data.size) {
            if (!newData.contains(data[i])) positionsOfDeletedItems.add(i)
        }
        positionsOfDeletedItems.reversed().forEach { position ->
            deleteItem(position)
        }
        return AdapterDataEvent.ITEMS_DELETED
    }

    private fun addItem(item: T) {
        data.add(item)
        notifyItemInserted(data.lastIndex)
        notifyItemRangeInserted(data.lastIndex, 1)
    }

    private fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, data.size)
    }

    /**
     * Base ViewHolder with selecting functionality. Triggers the listener and changes the UI of
     * selected items.
     */
    abstract inner class BaseSelectableViewHolder(
        private val view: View,
        private val listener: ItemSelectListener<T>
    ) : RecyclerView.ViewHolder(view),
        View.OnClickListener, View.OnLongClickListener {
        init {
            this.view.setOnClickListener(this@BaseSelectableViewHolder)
            this.view.setOnLongClickListener(this@BaseSelectableViewHolder)
        }

        override fun onClick(v: View?) {
            if (isSelecting) onLongClick(v)
            else listener.onItemClick(data[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            data[adapterPosition].let {
                if (this.view.isSelected) {
                    this.view.isSelected = false
                    setViewDeselectUi()
                    listener.onItemDeselect(it)
                } else {
                    this.view.isSelected = true
                    setViewSelectedUi()
                    listener.onItemSelect(it)
                }
                return true
            }
        }

        /**
         * Change the UI to the state where it is selected.
         */
        abstract fun setViewSelectedUi()

        /**
         * Change the UI to the state where it is deselected.
         */
        abstract fun setViewDeselectUi()
    }
}