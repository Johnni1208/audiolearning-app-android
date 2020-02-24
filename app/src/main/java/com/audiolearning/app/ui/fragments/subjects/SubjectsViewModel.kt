package com.audiolearning.app.ui.fragments.subjects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubjectsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Subjects Fragment"
    }
    val text: LiveData<String> = _text
}