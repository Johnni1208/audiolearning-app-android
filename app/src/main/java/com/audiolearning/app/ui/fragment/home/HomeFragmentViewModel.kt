package com.audiolearning.app.ui.fragment.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class HomeFragmentViewModel @ViewModelInject constructor() : ViewModel() {
    var previousFragmentPosition: Int? = null
}
