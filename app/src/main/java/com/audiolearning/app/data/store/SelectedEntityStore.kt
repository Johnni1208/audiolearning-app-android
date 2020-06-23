package com.audiolearning.app.data.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audiolearning.app.data.db.entities.BaseEntity

/**
 * Class for storing selected children of [BaseEntity].
 *
 * @param T specify Entity
 */
class SelectedEntityStore<T : BaseEntity> {
    private var mutableSelectedEntityList: ArrayList<T> = arrayListOf()
    private val _selectedEntityList: MutableLiveData<ArrayList<T>> =
        MutableLiveData<ArrayList<T>>().apply {
            value = arrayListOf()
        }

    val selectedEntityList: LiveData<ArrayList<T>>
        get() = _selectedEntityList

    fun select(entity: T) {
        mutableSelectedEntityList.add(entity)
        _selectedEntityList.postValue(mutableSelectedEntityList)
    }

    fun deselect(entity: T) {
        mutableSelectedEntityList.remove(entity)
        _selectedEntityList.postValue(mutableSelectedEntityList)
    }

    fun clear() {
        mutableSelectedEntityList.clear()
        _selectedEntityList.postValue(mutableSelectedEntityList)
    }
}
