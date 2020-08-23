package com.audiolearning.app.ui.fragment.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection

const val POSITION_ABOUT_US_FRAGMENT = 0
const val POSITION_RECORDER_FRAGMENT = 1
const val POSITION_SUBJECT_FRAGMENT = 2

class HomeFragmentViewModel @ViewModelInject constructor(
    // Must be init here, since else it is in an uninitialized state later
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData<String>().apply {
        value = ""
    }

    var previousFragmentPosition: Int = POSITION_RECORDER_FRAGMENT
}
