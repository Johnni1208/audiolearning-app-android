package com.example.audiolearning.ui.dialogs.new_recording

import com.example.audiolearning.models.Subject

interface NewRecordingDialogButtonsListener {
    fun onSaveButtonClicked(name: String, subject: Subject)
    fun onDiscardButtonClicked()
}