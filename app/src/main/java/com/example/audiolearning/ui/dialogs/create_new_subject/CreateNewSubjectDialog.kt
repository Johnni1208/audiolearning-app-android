package com.example.audiolearning.ui.dialogs.create_new_subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.R
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.repositories.SubjectRepository
import kotlinx.android.synthetic.main.dialog_create_new_subject.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * This shows a dialog, where the user can create a new subject.
 */
class CreateNewSubjectDialog : DialogFragment() {
    private lateinit var viewModel: CreateNewSubjectDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_DialogWithTitle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = CreateNewSubjectDialogViewModelFactory(
            SubjectRepository(
                AudioLearningDatabase.invoke(requireContext()),
                requireContext().filesDir
            )
        )

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateNewSubjectDialogViewModel::class.java)

        dialog!!.setTitle(R.string.cnsDialog_title)
        return inflater.inflate(R.layout.dialog_create_new_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cancel_subject.setOnClickListener { dismiss() }

        btn_save_subject.setOnClickListener {
            val subjectName = et_subject_name.text.toString()

            val inputValidation = viewModel.validateInput(subjectName)
            if (inputValidation != CreateNewSubjectInputValidation.CORRECT) {
                setError(inputValidation)
                return@setOnClickListener
            }

            GlobalScope.launch {
                viewModel.createNewSubject(subjectName)
                dismiss()
            }
        }
    }

    private fun setError(validation: CreateNewSubjectInputValidation) {
        when (validation) {
            CreateNewSubjectInputValidation.FIELD_CONTAINS_INVALID_CHARS -> et_subject_name.error =
                getString(R.string.dialog_error_message_contains_not_allowed_character)

            CreateNewSubjectInputValidation.FIELD_IS_BLANK -> et_subject_name.error =
                getString(R.string.dialog_error_message_missing_info)

            CreateNewSubjectInputValidation.SUBJECT_ALREADY_EXISTS -> et_subject_name.error =
                getString(R.string.cnsDialog_error_subject_already_exists)

            else -> {
            }
        }
    }
}