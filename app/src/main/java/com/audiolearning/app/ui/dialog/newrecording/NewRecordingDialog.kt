package com.audiolearning.app.ui.dialog.newrecording

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.audiolearning.app.R
import com.audiolearning.app.adapter.SubjectSpinnerAdapter
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.exception.MissingArgumentException
import com.audiolearning.app.extension.hideKeyboard
import com.audiolearning.app.ui.component.RoundedBottomSheetDialogFragment
import com.audiolearning.app.ui.dialog.genericyesno.DialogDataReceiver
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialog
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialogTexts
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_new_recording.btn_discard_recording
import kotlinx.android.synthetic.main.dialog_new_recording.btn_play_pause_audio
import kotlinx.android.synthetic.main.dialog_new_recording.btn_save_recording
import kotlinx.android.synthetic.main.dialog_new_recording.et_audio_name
import kotlinx.android.synthetic.main.dialog_new_recording.sp_select_subject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * Shown after the user has finished recording an audio.
 */
@AndroidEntryPoint
class NewRecordingDialog : RoundedBottomSheetDialogFragment(),
    DialogDataReceiver {
    private lateinit var newRecording: File
    private val simpleMediaPlayer = MediaPlayer()
    private lateinit var dialogContext: Context
    private lateinit var discardRecordingDialogTexts: GenericYesNoDialogTexts
    private var dialogRequestCode: Int = 0 // lateinit

    private val viewModel: NewRecordingDialogViewModel by viewModels()

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

        newRecording = viewModel.receiveNewRecordingFromArguments(
            arguments ?: throw MissingArgumentException(
                ""
            )
        )

        discardRecordingDialogTexts =
            GenericYesNoDialogTexts(
                getString(R.string.drDialog_title),
                getString(R.string.drDialog_message),
                getString(R.string.discard),
                getString(R.string.cancel)
            )

        dialogRequestCode =
            dialogContext.resources.getInteger(R.integer.request_code_nrDialog_discard_recording)
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
        viewModel.getSubjects().observe(viewLifecycleOwner, { subjects: List<Subject> ->
            val spinnerAdapter = SubjectSpinnerAdapter.createWithAddHint(
                dialogContext,
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
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.action != KeyEvent.ACTION_DOWN) return@OnKeyListener true

                showDiscardRecordingDialog()
                return@OnKeyListener true
            }

            return@OnKeyListener false
        })

        btn_discard_recording.setOnClickListener { showDiscardRecordingDialog() }

        // Save recording
        btn_save_recording.setOnClickListener {
            val name = et_audio_name.editText?.text.toString()
            val subject = sp_select_subject.selectedItem as Subject

            val inputValidation = runBlocking { viewModel.validateInput(name, subject) }
            if (inputValidation != NewRecordingInputValidation.CORRECT) {
                setError(inputValidation)
                return@setOnClickListener
            }

            MainScope().launch {
                viewModel.saveAudio(newRecording, name, subject)
                dismiss()
            }
        }

        simpleMediaPlayer.apply {
            setDataSource(newRecording.path)
            prepare()

            setOnCompletionListener {
                btn_play_pause_audio.setImageResource(R.drawable.ic_play)
            }
        }

        btn_play_pause_audio.setOnClickListener {
            if (!simpleMediaPlayer.isPlaying) {
                simpleMediaPlayer.start()
                btn_play_pause_audio.setImageResource(R.drawable.ic_pause)
            } else {
                simpleMediaPlayer.pause()
                btn_play_pause_audio.setImageResource(R.drawable.ic_play)
            }
        }
    }

    private fun showDiscardRecordingDialog() {
        GenericYesNoDialog.display(
            parentFragmentManager,
            discardRecordingDialogTexts,
            this,
            dialogRequestCode
        )
    }

    private fun setError(validation: NewRecordingInputValidation) {
        when (validation) {
            NewRecordingInputValidation.SUBJECT_IS_BLANK -> {
                (sp_select_subject.selectedView as TextView).error =
                    getString(R.string.error_missing_info)
            }

            NewRecordingInputValidation.NAME_IS_BLANK -> et_audio_name.error =
                getString(R.string.error_missing_info)

            NewRecordingInputValidation.NAME_CONTAINS_INVALID_CHARS -> et_audio_name.error =
                getString(R.string.error_contains_not_allowed_character)

            NewRecordingInputValidation.NAME_ALREADY_EXISTS_IN_SUBJECT -> et_audio_name.error =
                getString(R.string.error_audio_already_exists)

            else -> {
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        et_audio_name.hideKeyboard()
        newRecording.delete()
        simpleMediaPlayer.apply {
            stop()
            release()
        }
        super.onDismiss(dialog)
    }

    /**
     * Received from the DiscardDialog.
     */
    override fun onDialogResult(requestCode: Int, resultCode: Int) {
        if (requestCode != this.dialogRequestCode) return

        if (resultCode == Activity.RESULT_OK) dismiss()
    }
}
