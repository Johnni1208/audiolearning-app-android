package com.audiolearning.app.ui.fragment.audiosofsubject

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiolearning.app.R
import com.audiolearning.app.adapter.AdapterDataEvent
import com.audiolearning.app.adapter.recycler.selectable.AudiosRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.databinding.FragmentAudiosOfSubjectBinding
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerActivity
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerControlsViewModel
import com.audiolearning.app.ui.dialog.genericyesno.DialogDataReceiver
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialog
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialogTexts
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AudiosOfSubjectFragment : Fragment(),
    ItemSelectListener<Audio>,
    DialogDataReceiver {
    private var dialogRequestCode: Int = 0 // lateinit

    private val args: AudiosOfSubjectFragmentArgs by navArgs()

    private val viewModel: AudiosOfSubjectActivityViewModel by viewModels()
    private val audioPlayerControlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: FragmentAudiosOfSubjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_audios_of_subject,
            container,
            false
        )
        binding.lifecycleOwner = this

        dialogRequestCode =
            resources.getInteger(R.integer.request_code_audiosOfSubjectsActivity_delete_audios)

        setupSubject()
        setupToolbar()
        setupEmptyStateMessage()
        setupRecyclerView()

        return binding.root
    }

    private fun setupSubject() = runBlocking { viewModel.setSubject(args.SUBJECTID) }

    private fun setupToolbar() {
        binding.tbAudiosOfSubject.title = ""
        binding.ctb.title = viewModel.subject.value?.name

        // Change title if items are selected
        viewModel.selectedAudiosList.observe(viewLifecycleOwner, { list ->
            if (list.isNotEmpty()) {
                binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_cross)
                binding.ctb.title = list.size.toString()
            } else {
                binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_arrow_back)
                binding.ctb.title = viewModel.subject.value?.name
            }
        })

        // Change visibility of menu
        binding.tbAudiosOfSubject.inflateMenu(R.menu.delete_menu)
        val deleteItem = binding.tbAudiosOfSubject.menu.findItem(R.id.menu_item_delete)
        viewModel.selectedAudiosList.observe(viewLifecycleOwner, { list ->
            deleteItem?.isVisible = list.isNotEmpty()
        })

        // Handle Nav Icon click
        binding.tbAudiosOfSubject.setNavigationOnClickListener {
            if (viewModel.selectedAudiosList.value!!.isNotEmpty()) {
                deselectAllAudios()
            } else findNavController().navigateUp()
        }

        // Handle Menu Item clicks
        binding.tbAudiosOfSubject.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_item_delete) {
                requestDeletionOfSelectedAudios()
                return@setOnMenuItemClickListener true
            }

            return@setOnMenuItemClickListener false
        }
    }

    private fun deselectAllAudios() {
        if (viewModel.selectedAudiosList.value?.size!! == 0) return

        viewModel.deselectAllAudios()

        // Update RecyclerView items
        (binding.rvAudios.adapter as AudiosRecyclerViewAdapter).apply {
            notifyItemRangeChanged(0, this.itemCount)
        }
    }

    private fun requestDeletionOfSelectedAudios() {
        val deleteAudiosDialogText =
            GenericYesNoDialogTexts(
                getString(R.string.daDialog_title),
                getString(R.string.daDialog_message),
                getString(R.string.delete),
                getString(R.string.cancel)
            )

        GenericYesNoDialog.display(
            parentFragmentManager,
            deleteAudiosDialogText,
            this,
            dialogRequestCode
        )
    }

    private fun setupEmptyStateMessage() {
        viewModel.audios.observe(viewLifecycleOwner, Observer { audios: List<Audio> ->
            if (audios.isEmpty()) {
                binding.tvNoAudios.show()

                // Don't scroll the toolbar
                (binding.ctb.layoutParams as AppBarLayout.LayoutParams).scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                return@Observer
            }

            binding.tvNoAudios.hide()
        })
    }

    private fun setupRecyclerView() {
        val audioAdapter =
            AudiosRecyclerViewAdapter(
                this
            )

        // Update adapters data
        viewModel.audios.observe(viewLifecycleOwner, { audios: List<Audio> ->
            if (audioAdapter.isDataInitialized) {
                when (audioAdapter.updateData(audios)) {
                    AdapterDataEvent.ITEMS_ADDED ->
                        binding.rvAudios.smoothScrollToPosition(audios.size - 1)

                    AdapterDataEvent.ITEMS_DELETED ->
                        binding.rvAudios.smoothScrollToPosition(0)
                }
            } else audioAdapter.initializeData(audios)
        })

        // Update selecting state
        viewModel.selectedAudiosList.observe(
            viewLifecycleOwner,
            { selectedAudiosList: ArrayList<Audio> ->
                audioAdapter.isSelecting = selectedAudiosList.isNotEmpty()
            }
        )

        binding.rvAudios.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = audioAdapter
        }
    }

    override fun onItemSelect(item: Audio) {
        viewModel.selectAudio(item)
    }

    override fun onItemDeselect(item: Audio) {
        viewModel.deselectAudio(item)
    }

    override fun onItemClick(item: Audio) {
        MainScope().launch {
            audioPlayerControlsViewModel.playAudio(item)
        }

        Intent(context, AudioPlayerActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int) {
        if (requestCode != dialogRequestCode) return

        if (resultCode == Activity.RESULT_OK) {
            MainScope().launch {
                viewModel.deleteAllSelectedAudios()
            }
        }
    }
}
