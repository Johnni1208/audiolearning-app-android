package com.example.audiolearning.components.dialogs.new_recording

import com.example.audiolearning.models.Subject

interface NewRecordingDialogButtonsListener {
    fun onAddButtonClicked(name: String, subject: Subject)
}