package com.audiolearning.app.ui.fragments.subjects

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.audiolearning.app.R
import com.audiolearning.app.adapters.SubjectsRecyclerViewAdapter
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.databinding.FragmentSubjectsBinding
import com.audiolearning.app.extensions.addIfNotContained
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.removeIfContained
import com.audiolearning.app.extensions.show
import com.audiolearning.app.ui.activities.MainActivity
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialog
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialogTexts
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SubjectsFragment : DaggerFragment(), SubjectsRecyclerViewAdapter.SubjectClickListener {
    private val subjectsSelectedList: ArrayList<Subject> = arrayListOf()
    private var dialogRequestCode: Int = 0 // Initialized later
    private lateinit var deleteSubjectsDialogTexts: DefaultYesNoDialogTexts

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SubjectsViewModel> { viewModelFactory }
    private lateinit var binding: FragmentSubjectsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_subjects,
            container,
            false
        )

        binding.lifecycleOwner = this

        dialogRequestCode =
            resources.getInteger(R.integer.request_code_subjectFragment_delete_subject)

        deleteSubjectsDialogTexts = DefaultYesNoDialogTexts(
            getString(R.string.dsDialog_title),
            getString(R.string.dsDialog_message),
            getString(R.string.dsDialog_positive_button_text),
            getString(R.string.cancel)
        )

        setupEmptyStateMessage()
        setupRecyclerView()
        setupFab()

        return binding.root
    }

    private fun setupEmptyStateMessage() {
        // Updates the empty state message
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            if (subjects.isEmpty()) binding.tvNoSubjects.show()
            else binding.tvNoSubjects.hide()
        })
    }

    private fun setupRecyclerView() {
        val subjectsAdapter =
            SubjectsRecyclerViewAdapter(viewModel.getSubjects().value ?: emptyList(), this)

        // Updates the adapter with new data
        var subjectListCount = 0
        var initialized = false
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            subjectsAdapter.setData(subjects)

            if (initialized && subjects.size > subjectListCount) binding.rvSubjects.smoothScrollToPosition(
                subjects.size - 1
            )

            subjectListCount = subjects.size
            initialized = true
        })

        binding.rvSubjects.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = subjectsAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddSubject.setOnClickListener {
            CreateNewSubjectDialog().show(
                parentFragmentManager,
                "SubjectsFragment"
            )
        }
    }

    override fun onSubjectItemClick(position: Int) = runBlocking {
        val subject: Subject = viewModel.getSubjectById(position)
        if (onSubjectItemDeselect(subject)) return@runBlocking
    }

    private fun onSubjectItemDeselect(subject: Subject): Boolean {
        val isRemoved: Boolean = subjectsSelectedList.removeIfContained(subject)
        if (subjectsSelectedList.isEmpty()) MainActivity.hideDeleteIcon()
        return isRemoved
    }

    override fun onSubjectItemLongClick(position: Int): Boolean = runBlocking {
        val subject: Subject = viewModel.getSubjectById(position)

        if (subjectsSelectedList.addIfNotContained(subject)) {
            MainActivity.showDeleteIcon()
            return@runBlocking true
        }

        return@runBlocking false
    }

    fun requestDeletionOfSubjects() = GlobalScope.launch {
        DefaultYesNoDialog.display(
            parentFragmentManager,
            deleteSubjectsDialogTexts,
            this@SubjectsFragment,
            dialogRequestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != dialogRequestCode) return

        if (resultCode == Activity.RESULT_OK) {
            GlobalScope.launch {
                subjectsSelectedList.forEach {
                    viewModel.deleteSubject(it)
                }
                subjectsSelectedList.clear()
            }
            MainActivity.hideDeleteIcon()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}