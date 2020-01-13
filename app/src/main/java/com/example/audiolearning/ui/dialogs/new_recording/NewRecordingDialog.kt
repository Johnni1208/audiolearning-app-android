package com.example.audiolearning.ui.dialogs.new_recording

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.audiolearning.R
import com.example.audiolearning.data.db.entities.Subject
import kotlinx.android.synthetic.main.dialog_new_recording.*

class NewRecordingDialog(
    private var newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener
) : DialogFragment() {

    companion object {
        fun display(
            newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener,
            fragmentManager: FragmentManager
        ) = NewRecordingDialog(newRecordingDialogButtonsListener).show(
            fragmentManager,
            "newrecordingdialog"
        )
    }

    override fun onStart() {
        super.onStart()
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = ViewGroup.LayoutParams.MATCH_PARENT

        dialog!!.window!!.let {
            it.setLayout(width, height)
            it.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_new_recording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        nr_toolbar.setNavigationOnClickListener {
            newRecordingDialogButtonsListener.onDiscardButtonClicked()
            dismiss()
        }

        btn_discard_recording.setOnClickListener {
            newRecordingDialogButtonsListener.onDiscardButtonClicked()
            dismiss()
        }

        btn_save_recording.setOnClickListener {
            val name = et_audio_name.text.toString()
//            val subject = sp_audio_subject.selectedItem as Subject
            val subject = Subject(
                "test",
                Environment.getExternalStorageDirectory()
            ).apply {
                id = 1
            }

            if (name.isEmpty()) {
                et_audio_name.error = getString(R.string.nrDialog_error_message_missing_info)
                return@setOnClickListener
            }

            if (subject.name.isEmpty()) {
                (sp_audio_subject.selectedView as TextView).error =
                    getString(R.string.nrDialog_error_message_missing_info)
                return@setOnClickListener
            }

            newRecordingDialogButtonsListener.onSaveButtonClicked(name, subject)
        }
    }
}