package com.audiolearning.app.adapter.recycler.selectable.base

import com.audiolearning.app.data.db.entities.BaseEntity

interface ItemSelectListener<T : BaseEntity> {
    fun onItemSelect(item: T)
    fun onItemDeselect(item: T)
    fun onItemClick(item: T)
}
