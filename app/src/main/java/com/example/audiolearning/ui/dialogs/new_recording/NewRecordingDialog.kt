package com.example.audiolearning.ui.dialogs.new_recording

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.audiolearning.R
import com.example.audiolearning.adapters.SubjectArrayAdapterFactory
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.extensions.isAllowedFileName
import com.example.audiolearning.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import kotlinx.android.synthetic.main.dialog_new_recording.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewRecordingDialog(
    private var newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener
) : DialogFragment() {
    private lateinit var dialogContext: Context

    companion object {
        fun display(
            newRecordingDialogButtonsListener: NewRecordingDialogButtonsListener,
            fragmentManager: FragmentManager
        ) = NewRecordingDialog(newRecordingDialogButtonsListener).show(
            fragmentManager,
            "NewRecordingDialog"
        )
    }

    override fun onStart() {
        super.onStart()
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = ViewGroup.LayoutParams.MATCH_PARENT

        dialog!!.window!!.let {
            it.setLayout(width, height)
            it.setWindowAnimations(R.style.AppTheme_SlideAnimation)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogContext = context
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
        GlobalScope.launch {
            setupSpinner()
        }
        setupOnClickListeners()
    }

    private suspend fun setupSpinner() {
//        val subjects = SubjectRepository(AudioLearningDatabase.invoke(dialogContext)).getAllSubjects()

        // Testing purposes
        val subjects = listOf(
            Subject("test1", "abc"),
            Subject("testkehebjhkbjhkjhkjhgjhkgjhgjhkgjhkgjhkgjhkgjhk2", "abc")
        )
        val spinnerAdapter = SubjectArrayAdapterFactory.createWithAddHint(
            dialogContext,
            R.layout.subject_spinner_item,
            subjects
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        sp_audio_subject.adapter = spinnerAdapter
        sp_audio_subject.setSelection(spinnerAdapter.count)

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    CreateNewSubjectDialog().show(
                        requireFragmentManager(),
                        "NewRecordingDialog"
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        sp_audio_subject.onItemSelectedListener = onItemSelectedListener
    }

    private fun setupOnClickListeners() {
        // Discard recording
        nr_toolbar.setNavigationOnClickListener {
            newRecordingDialogButtonsListener.onDiscardButtonClicked()
            dismiss()
        }

        btn_discard_recording.setOnClickListener {
            newRecordingDialogButtonsListener.onDiscardButtonClicked()
            dismiss()
        }

        // Save recording
        btn_save_recording.setOnClickListener {
            val name = et_audio_name.text.toString()
            val subject = sp_audio_subject.selectedItem as Subject

            if (!isInputValid(name, subject)) return@setOnClickListener

            newRecordingDialogButtonsListener.onSaveButtonClicked(name, subject)
            dismiss()
        }
    }

    private fun isInputValid(name: String, subject: Subject): Boolean {
        if (!isNameValid(name)) return false

        if (!subject.isRealSubject) {
            (sp_audio_subject.selectedView as TextView).error =
                getString(R.string.dialog_error_message_missing_info)
            return false
        }

        return true
    }

    private fun isNameValid(name: String): Boolean {
        if (name.isEmpty()) {
            et_audio_name.error = getString(R.string.dialog_error_message_missing_info)
            return false
        }

        if (!name.isAllowedFileName()) {
            et_audio_name.error =
                getString(R.string.dialog_error_message_contains_not_allowed_character)
            return false
        }

        return true
    }
}