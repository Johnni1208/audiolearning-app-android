package com.audiolearning.app.ui.fragment.subjects

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.audiolearning.app.R
import com.audiolearning.app.adapter.AdapterDataEvent
import com.audiolearning.app.adapter.recycler.selectable.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.databinding.FragmentSubjectsBinding
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import com.audiolearning.app.ui.activity.MainActivityToolBarChangeListener
import com.audiolearning.app.ui.activity.audiosofsubject.AudiosOfSubjectActivity
import com.audiolearning.app.ui.dialog.createnewsubject.CreateNewSubjectDialog
import com.audiolearning.app.ui.dialog.genericyesno.DialogDataReceiver
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialog
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialogTexts
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubjectsFragment(private val toolBarChangeListener: MainActivityToolBarChangeListener) :
    Fragment(),
    ItemSelectListener<Subject>,
    DialogDataReceiver {
    private var dialogRequestCode: Int = 0 // lateinit

    private val viewModel: SubjectsFragmentViewModel by viewModels()
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

        setupEmptyStateMessage()
        setupRecyclerView()
        setupFab()
        setupSelectedSubjectsToolbar()

        return binding.root
    }

    private fun setupEmptyStateMessage() {
        // Updates the empty state message
        viewModel.subjects.observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            if (subjects.isEmpty()) binding.tvNoSubjects.show()
            else binding.tvNoSubjects.hide()
        })
    }

    private fun setupRecyclerView() {
        val subjectsAdapter =
            SubjectsRecyclerViewAdapter(
                this
            )

        // Update adapters data
        viewModel.subjects.observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
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
            }
        )

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

    override fun onDialogResult(requestCode: Int, resultCode: Int) {
        if (requestCode != dialogRequestCode) return

        if (resultCode == Activity.RESULT_OK) {
            MainScope().launch {
                viewModel.deleteAllSelectedSubjects()
            }
        }
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
        val deleteSubjectsDialogTexts =
            GenericYesNoDialogTexts(
                getString(R.string.dsDialog_title),
                getString(R.string.dsDialog_message),
                getString(R.string.delete),
                getString(R.string.cancel)
            )

        GenericYesNoDialog.display(
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
