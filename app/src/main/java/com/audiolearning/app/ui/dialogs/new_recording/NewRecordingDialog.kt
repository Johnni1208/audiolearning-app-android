package com.audiolearning.app.ui.dialogs.new_recording

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
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
import com.audiolearning.app.extensions.hideKeyboard
import com.audiolearning.app.extensions.showKeyboard
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialog
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialogTexts
import com.audiolearning.app.util.ArgumentMissingException
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_new_recording.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class NewRecordingDialog : DaggerDialogFragment() {
    private lateinit var newRecording: File
    private lateinit var dialogContext: Context
    private lateinit var discardRecordingDialogTexts: DefaultYesNoDialogTexts

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<NewRecordingDialogViewModel> { viewModelFactory }

    companion object {
        const val ARG_NEW_FILE_PATH = "NewRecordingDialog.newFilePath"

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
                putString(ARG_NEW_FILE_PATH, newFilePath)
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

        val newRecordingFilePath = arguments?.getString(ARG_NEW_FILE_PATH)
            ?: throw ArgumentMissingException(ARG_NEW_FILE_PATH)
        if (newRecordingFilePath.isEmpty()) throw ArgumentMissingException(ARG_NEW_FILE_PATH)

        newRecording = File(newRecordingFilePath)

        discardRecordingDialogTexts = DefaultYesNoDialogTexts(
            getString(R.string.drDialog_title),
            getString(R.string.drDialog_message),
            getString(R.string.drDialog_positive_button_text),
            getString(R.string.cancel)
        )
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
        et_audio_name.showKeyboard()
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
        dialog?.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN) return@OnKeyListener true

            if (keyCode == KeyEvent.KEYCODE_BACK) showDiscardRecordingDialog()
            return@OnKeyListener true
        })

        nr_toolbar.setNavigationOnClickListener {
            showDiscardRecordingDialog()
        }

        btn_discard_recording.setOnClickListener {
            showDiscardRecordingDialog()
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

    private fun showDiscardRecordingDialog() {
        DefaultYesNoDialog.display(
            parentFragmentManager,
            discardRecordingDialogTexts,
            this
        )
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

    override fun onDismiss(dialog: DialogInterface) {
        et_audio_name.hideKeyboard()
        newRecording.delete()
        super.onDismiss(dialog)
    }

    /**
     * Receives Results from the [DefaultYesNoDialog] started when trying to dismiss the dialog.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != DefaultYesNoDialog.YES_NO_CALL) return

        if (resultCode == Activity.RESULT_OK) dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }
}