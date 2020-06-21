package com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter

import com.audiolearning.app.data.db.entities.BaseEntity

interface ItemSelectListener<T : BaseEntity> {
    fun onItemSelect(item: T)
    fun onItemDeselect(item: T)
    fun onItemClick(item: T)
}