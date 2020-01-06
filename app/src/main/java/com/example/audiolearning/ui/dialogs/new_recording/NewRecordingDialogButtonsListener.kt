package com.example.audiolearning.ui.dialogs.new_recording

import com.example.audiolearning.models.Subject

interface NewRecordingDialogButtonsListener {
    fun onAddButtonClicked(name: String, subject: Subject)
}