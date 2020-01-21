package com.example.audiolearning.ui.dialogs.create_new_subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.audiolearning.R
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.repositories.SubjectRepository
import kotlinx.android.synthetic.main.dialog_create_new_subject.*

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
                AudioLearningDatabase.invoke(requireContext())
            )
        )

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CreateNewSubjectDialogViewModel::class.java)

        dialog!!.setTitle(R.string.cnsDialog_title)
        return inflater.inflate(R.layout.dialog_create_new_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cancel_subject.setOnClickListener { dismiss() }

        btn_save_subject.setOnClickListener {
            val subjectName = et_subject_name.text.toString()
            if (viewModel.createNewSubject(requireContext().filesDir, subjectName)) dismiss()
        }

        observeError()
    }

    private fun observeError() = viewModel.error.observe(this, Observer {
        if (it != null) {
            et_subject_name.error = getString(it)
        }
    })
}