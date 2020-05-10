package com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter

interface ItemSelectListener {
    fun onItemDeselect(id: Int)
    fun onItemSelect(id: Int)
    fun onItemClick(id: Int)
}