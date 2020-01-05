package com.example.audiolearning.components.dialogs.new_recording

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.audiolearning.R
import com.example.audiolearning.models.Subject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_new_recording.view.*

class NewRecordingDialog(
    var newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener
) : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dialogView = it.layoutInflater.inflate(R.layout.dialog_new_recording, null)

            return MaterialAlertDialogBuilder(it)
                .setView(dialogView)
                .setTitle(R.string.nrDialog_title)
                .setPositiveButton(R.string.nrDialog_positive_button_text) { _, _ ->
                    val name = dialogView.et_audio_name.text.toString()
                    val subject = Subject("test")

                    newRecordingDialogButtonsListener.onAddButtonClicked(name, subject)
                }
                .setNegativeButton(R.string.nrDialog_negative_button_text) { _, _ -> this.dialog!!.cancel() }
                .create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }


}