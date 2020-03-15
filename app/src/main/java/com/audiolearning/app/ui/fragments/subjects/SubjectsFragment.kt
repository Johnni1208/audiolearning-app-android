package com.audiolearning.app.ui.fragments.subjects

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
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SubjectsFragment : DaggerFragment(), SubjectsRecyclerViewAdapter.SubjectClickListener {
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
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            subjectsAdapter.setData(subjects)

            if (subjects.size > subjectListCount) binding.rvSubjects.smoothScrollToPosition(subjects.size - 1)
            subjectListCount = subjects.size
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
    }

    override fun onSubjectItemLongClick(position: Int): Boolean = runBlocking {
        val subject = viewModel.getSubjectById(position)
            ?: throw IllegalArgumentException("Could not find subject with id $position")
        viewModel.deleteSubject(subject)
        return@runBlocking true
    }
}