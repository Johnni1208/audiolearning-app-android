package com.example.audiolearning.ui.dialogs.new_recording

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.audiolearning.R
import com.example.audiolearning.adapters.SubjectArrayAdapter
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.data.repositories.SubjectRepository
import com.example.audiolearning.extensions.isAllowedFileName
import kotlinx.android.synthetic.main.dialog_new_recording.*
import java.io.File

class NewRecordingDialog(
    private var newRecording: File
) : DialogFragment() {

    private lateinit var dialogContext: Context
    private lateinit var viewModel: NewRecordingDialogViewModel

    companion object {
        fun display(
            newFile: File,
            fragmentManager: FragmentManager
        ) = NewRecordingDialog(newFile).show(
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
        val audioLearningDatabase = AudioLearningDatabase.invoke(dialogContext)

        val viewModelFactory = NewRecordingDialogViewModelFactory(
            SubjectRepository(audioLearningDatabase), AudioRepository(audioLearningDatabase)
        )

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewRecordingDialogViewModel::class.java)

        return inflater.inflate(R.layout.dialog_new_recording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupOnClickListeners()
    }

    private fun setupSpinner() {

        sp_audio_subject.onItemSelectedListener =
            viewModel.getAddHintItemSelectedListener(requireFragmentManager())

        var timesLoadedNewSubjects = 0

        viewModel.getSubjects().observe(this, Observer { subjects ->
            val spinnerAdapter = SubjectArrayAdapter.createWithAddHint(
                dialogContext,
                R.layout.subject_spinner_item,
                subjects,
                true
            )

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_audio_subject.adapter = spinnerAdapter

            if (timesLoadedNewSubjects == 0) {
                sp_audio_subject.setSelection(spinnerAdapter.count)
            } else sp_audio_subject.setSelection(spinnerAdapter.count - 1)

            timesLoadedNewSubjects++
        })

    }

    private fun setupOnClickListeners() {
        // Discard recording
        nr_toolbar.setNavigationOnClickListener {
            newRecording.delete()
            dismiss()
        }

        btn_discard_recording.setOnClickListener {
            newRecording.delete()
            dismiss()
        }

        // Save recording
        btn_save_recording.setOnClickListener {
            val name = et_audio_name.text.toString()
            val subject = sp_audio_subject.selectedItem as Subject

            if (!isInputValid(name, subject)) return@setOnClickListener

            viewModel.saveAudio(newRecording, name, subject)
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