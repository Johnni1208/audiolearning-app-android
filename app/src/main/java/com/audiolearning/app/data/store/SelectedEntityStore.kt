package com.audiolearning.app.data.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audiolearning.app.data.db.entities.BaseEntity
import com.audiolearning.app.extensions.addIfNotContained
import com.audiolearning.app.extensions.removeIfContained

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

    fun select(entity: T): Boolean {
        if (mutableSelectedEntityList.addIfNotContained(entity)) {
            _selectedEntityList.postValue(mutableSelectedEntityList)
            return true
        }

        return false
    }

    fun deselect(entity: T): Boolean {
        if (mutableSelectedEntityList.removeIfContained(entity)) {
            _selectedEntityList.postValue(mutableSelectedEntityList)
            return true
        }

        return false
    }

    fun clear() {
        mutableSelectedEntityList.clear()
        _selectedEntityList.postValue(mutableSelectedEntityList)
    }
}