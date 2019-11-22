package com.example.audiolearning.fragments.recorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecorderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Recorder Fragment"
    }
    val text: LiveData<String> = _text
}