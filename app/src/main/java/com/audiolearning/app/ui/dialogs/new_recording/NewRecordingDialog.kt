package com.audiolearning.app.ui.dialogs.new_recording

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audiolearning.app.R
import com.audiolearning.app.adapters.SubjectArrayAdapter
import com.audiolearning.app.data.db.entities.Subject
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_new_recording.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class NewRecordingDialog : DaggerDialogFragment() {
    private lateinit var newRecording: File
    private lateinit var dialogContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<NewRecordingDialogViewModel> { viewModelFactory }

    companion object {
        const val newFilePathArgumentKey = "newFilePath"

        /**
         * Bundles the newFilePath and puts it as the fragments arguments.
         * It then shows it.
         */
        fun display(
            newFilePath: String,
            fragmentManager: FragmentManager
        ) {
            val dialog = NewRecordingDialog()

            dialog.arguments = Bundle().apply {
                putString(newFilePathArgumentKey, newFilePath)
            }

            dialog.show(
                fragmentManager,
                "NewRecordingDialog"
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)

        val newRecordingFilePath = arguments?.getString(newFilePathArgumentKey)
            ?: throw IllegalArgumentException("No argument provided for the new recording")
        if (newRecordingFilePath.isEmpty()) throw IllegalArgumentException("No argument provided for the new recording")

        newRecording = File(newRecordingFilePath)
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
        setupSpinner()
        setupOnClickListeners()
    }

    private fun setupSpinner() {
        sp_select_subject.onItemSelectedListener =
            viewModel.getAddHintItemSelectedListener(parentFragmentManager)

        var hasNewSubject = false

        /* Observes if there are new subject items.
         * If so, it creates a new array adapter with the items
         * and then loads it into the spinner.
         */
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            val spinnerAdapter = SubjectArrayAdapter.createWithAddHint(
                dialogContext,
                R.layout.subject_spinner_item,
                subjects,
                true
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            sp_select_subject.adapter = spinnerAdapter

            if (!hasNewSubject) {
                sp_select_subject.setSelection(spinnerAdapter.count) // "Select subject"
            } else sp_select_subject.setSelection(spinnerAdapter.count - 1) // New subject name

            hasNewSubject = true
        })
    }

    private fun setupOnClickListeners() {
        // Discard recording
        nr_toolbar.setNavigationOnClickListener {
            newRecording.delete()
            dismiss()
        }

        // Discard recording
        btn_discard_recording.setOnClickListener {
            newRecording.delete()
            dismiss()
        }

        // Save recording
        btn_save_recording.setOnClickListener {
            val name = et_audio_name.text.toString()
            val subject = sp_select_subject.selectedItem as Subject

            val inputValidation = viewModel.validateInput(name, subject)
            if (inputValidation != NewRecordingInputValidation.CORRECT) {
                setError(inputValidation)
                return@setOnClickListener
            }

            GlobalScope.launch {
                viewModel.saveAudio(newRecording, name, subject)
                dismiss()
            }
        }
    }

    private fun setError(validation: NewRecordingInputValidation) {
        when (validation) {
            NewRecordingInputValidation.SUBJECT_IS_BLANK -> {
                (sp_select_subject.selectedView as TextView).error =
                    getString(R.string.dialog_error_message_missing_info)
            }

            NewRecordingInputValidation.NAME_IS_BLANK -> et_audio_name.error =
                getString(R.string.dialog_error_message_missing_info)

            NewRecordingInputValidation.NAME_CONTAINS_INVALID_CHARS -> et_audio_name.error =
                getString(R.string.dialog_error_message_contains_not_allowed_character)

            else -> {
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = ViewGroup.LayoutParams.MATCH_PARENT

        dialog?.window?.let {
            it.setLayout(width, height)
            it.setWindowAnimations(R.style.AppTheme_SlideAnimation)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        newRecording.delete()
        super.onCancel(dialog)
    }
}