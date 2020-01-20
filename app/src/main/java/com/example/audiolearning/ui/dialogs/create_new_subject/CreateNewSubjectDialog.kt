package com.example.audiolearning.ui.dialogs.create_new_subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.audiolearning.R
import com.example.audiolearning.extensions.isAllowedFileName
import kotlinx.android.synthetic.main.dialog_create_new_subject.*

class CreateNewSubjectDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_DialogWithTitle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.setTitle(R.string.cnsDialog_title)
        return inflater.inflate(R.layout.dialog_create_new_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cancel_subject.setOnClickListener { dismiss() }

        btn_save_subject.setOnClickListener {
            if (createNewSubject()) dismiss()
        }
    }

    private fun createNewSubject(): Boolean {
        val subjectName = et_subject_name.text.toString()
        if (subjectName.isEmpty()) {
            et_subject_name.error = getString(R.string.dialog_error_message_missing_info)
            return false
        }

        if (!subjectName.isAllowedFileName()) {
            et_subject_name.error =
                getString(R.string.dialog_error_message_contains_not_allowed_character)
            return false
        }

        return true
    }
}