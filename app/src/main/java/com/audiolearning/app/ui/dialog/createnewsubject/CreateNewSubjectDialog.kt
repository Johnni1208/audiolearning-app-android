package com.audiolearning.app.ui.dialog.createnewsubject

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.audiolearning.app.R
import com.audiolearning.app.extension.hideKeyboard
import com.audiolearning.app.extension.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_create_new_subject.btn_cancel_subject
import kotlinx.android.synthetic.main.dialog_create_new_subject.btn_save_subject
import kotlinx.android.synthetic.main.dialog_create_new_subject.et_subject_name
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * This shows a dialog, where the user can create a new subject.
 */
@AndroidEntryPoint
class CreateNewSubjectDialog : DialogFragment() {
    private val viewModel: CreateNewSubjectDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_DialogWithTitle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.cnsDialog_title)
        return inflater.inflate(R.layout.dialog_create_new_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.showKeyboard(et_subject_name)

        et_subject_name.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    btn_save_subject.callOnClick()
                    true
                }
                else -> false
            }
        }

        btn_cancel_subject.setOnClickListener { dismiss() }

        btn_save_subject.setOnClickListener {
            val subjectName = et_subject_name.text.toString()

            val inputValidation = runBlocking { viewModel.validateInput(subjectName) }
            if (inputValidation != CreateNewSubjectInputValidation.CORRECT) {
                setError(inputValidation)
                return@setOnClickListener
            }

            MainScope().launch {
                viewModel.createNewSubject(subjectName)
                dismiss()
            }
        }
    }

    private fun setError(validation: CreateNewSubjectInputValidation) {
        when (validation) {
            CreateNewSubjectInputValidation.INPUT_FIELD_CONTAINS_INVALID_CHARS -> et_subject_name.error =
                getString(R.string.error_contains_not_allowed_character)

            CreateNewSubjectInputValidation.INPUT_FIELD_IS_BLANK -> et_subject_name.error =
                getString(R.string.error_missing_info)

            CreateNewSubjectInputValidation.SUBJECT_ALREADY_EXISTS -> et_subject_name.error =
                getString(R.string.error_subject_already_exists)

            else -> {
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        this.dialog?.window?.hideKeyboard()
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        this.dialog?.window?.hideKeyboard()
        super.onCancel(dialog)
    }
}
