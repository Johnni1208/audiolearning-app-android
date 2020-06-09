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
import com.audiolearning.app.adapters.AdapterDataEvent
import com.audiolearning.app.adapters.recycler_view_adapter.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.databinding.FragmentSubjectsBinding
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import com.audiolearning.app.ui.activities.MainActivityToolBarChangeListener
import com.audiolearning.app.ui.activities.audios_of_subject.AudiosOfSubjectActivity
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialog
import com.audiolearning.app.ui.dialogs.generic_yes_no_dialog.DefaultYesNoDialogTexts
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubjectsFragment(private val toolBarChangeListener: MainActivityToolBarChangeListener) :
    DaggerFragment(),
    ItemSelectListener<Subject> {
    private var dialogRequestCode: Int = 0 // lateinit
    private lateinit var deleteSubjectsDialogTexts: DefaultYesNoDialogTexts

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SubjectsFragmentViewModel> { viewModelFactory }
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
            getString(R.string.delete),
            getString(R.string.cancel)
        )

        setupEmptyStateMessage()
        setupRecyclerView()
        setupFab()
        setupSelectedSubjectsToolbar()

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
        val subjectsAdapter = SubjectsRecyclerViewAdapter(this)

        // Update adapters data
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            if (subjectsAdapter.isDataInitialized) {
                when (subjectsAdapter.updateData(subjects)) {
                    AdapterDataEvent.ITEMS_ADDED ->
                        binding.rvSubjects.smoothScrollToPosition(subjects.size - 1)

                    AdapterDataEvent.ITEMS_DELETED ->
                        binding.rvSubjects.smoothScrollToPosition(0)
                }
            } else subjectsAdapter.initializeData(subjects)
        })

        // Update selecting state
        viewModel.selectedSubjectsList.observe(
            viewLifecycleOwner,
            Observer { selectedSubjectsList: ArrayList<Subject> ->
                subjectsAdapter.isSelecting = selectedSubjectsList.isNotEmpty()
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

    private fun setupSelectedSubjectsToolbar() {
        viewModel.selectedSubjectsList.observe(
            viewLifecycleOwner,
            Observer { selectedSubjectsList: ArrayList<Subject> ->
                toolBarChangeListener.onSelectedSubjectsChange(
                    selectedSubjectsList
                )
            })
    }

    override fun onItemSelect(item: Subject) {
        viewModel.selectSubject(item)
    }

    override fun onItemDeselect(item: Subject) {
        viewModel.deselectSubject(item)
    }

    override fun onItemClick(item: Subject) {
        Intent(context, AudiosOfSubjectActivity::class.java).apply {
            putExtra(AudiosOfSubjectActivity.EXTRA_SUBJECT_ID, item.id)
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != dialogRequestCode) return

        if (resultCode == Activity.RESULT_OK) {
            MainScope().launch {
                viewModel.deleteAllSelectedSubjects()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        // Reloads Items (necessary on older devices)
        (binding.rvSubjects.adapter as SubjectsRecyclerViewAdapter).apply {
            notifyItemRangeChanged(0, this.itemCount)
        }
        super.onResume()
    }

    override fun onPause() {
        deselectAllSubjects()
        super.onPause()
    }

    fun requestDeletionOfSelectedSubjects() {
        DefaultYesNoDialog.display(
            parentFragmentManager,
            deleteSubjectsDialogTexts,
            this@SubjectsFragment,
            dialogRequestCode
        )
    }

    fun deselectAllSubjects() {
        if (viewModel.selectedSubjectsList.value?.size!! == 0) return

        viewModel.deselectAllSubjects()

        // Update RecyclerView items
        (binding.rvSubjects.adapter as SubjectsRecyclerViewAdapter).apply {
            notifyItemRangeChanged(0, this.itemCount)
        }
    }
}