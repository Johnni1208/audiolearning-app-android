package com.audiolearning.app.ui.activities.audios_of_subject

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiolearning.app.R
import com.audiolearning.app.adapters.AdapterDataEvent
import com.audiolearning.app.adapters.recycler_view_adapter.AudioRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.databinding.ActivityAudiosOfSubjectBinding
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import com.audiolearning.app.ui.components.generic_yes_no_dialog.DialogDataReceiver
import com.audiolearning.app.ui.components.generic_yes_no_dialog.GenericYesNoDialog
import com.audiolearning.app.ui.components.generic_yes_no_dialog.GenericYesNoDialogTexts
import com.audiolearning.app.util.MissingArgumentException
import com.google.android.material.appbar.AppBarLayout
import dagger.android.AndroidInjection
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AudiosOfSubjectActivity : AppCompatActivity(), ItemSelectListener<Audio>, DialogDataReceiver {
    private var dialogRequestCode: Int = 0 // lateinit
    private lateinit var deleteAudiosDialogText: GenericYesNoDialogTexts

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<AudiosOfSubjectActivityViewModel> { viewModelFactory }
    private lateinit var binding: ActivityAudiosOfSubjectBinding

    companion object {
        const val EXTRA_SUBJECT_ID = "extra_subject_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audios_of_subject)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        dialogRequestCode =
            resources.getInteger(R.integer.request_code_audiosOfSubjectsActivity_delete_audios)

        deleteAudiosDialogText =
            GenericYesNoDialogTexts(
                getString(R.string.daDialog_title),
                getString(R.string.daDialog_message),
                getString(R.string.delete),
                getString(R.string.cancel)
            )

        setupSubject()
        setupToolbar()
        setupEmptyStateMessage()
        setupRecyclerView()
    }

    private fun setupSubject() = runBlocking {
        viewModel.setSubject(
            intent.extras?.getInt(EXTRA_SUBJECT_ID)
                ?: throw MissingArgumentException(EXTRA_SUBJECT_ID)
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbAudiosOfSubject)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.selectedAudiosList.observe(this, Observer { list ->
            if (list.isNotEmpty()) binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_cross_24dp)
            else binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        })
    }

    private fun setupEmptyStateMessage() {
        viewModel.getAudios().observe(this, Observer { audios: List<Audio> ->
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
        val audioAdapter = AudioRecyclerViewAdapter(this)

        // Update adapters data
        viewModel.getAudios().observe(this, Observer { audios: List<Audio> ->
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
            this,
            Observer { selectedAudiosList: ArrayList<Audio> ->
                audioAdapter.isSelecting = selectedAudiosList.isNotEmpty()
            })

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
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)

        // Show / Hide deleteIcon, whether there are selected items or not
        val deleteItem = menu?.findItem(R.id.menu_item_delete)
        viewModel.selectedAudiosList.observe(this, Observer { list ->
            deleteItem?.isVisible = list.isNotEmpty()
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (viewModel.selectedAudiosList.value!!.isNotEmpty()) {
                    deselectAllAudios()
                    return true
                }

                onBackPressed()
                return true
            }

            R.id.menu_item_delete -> requestDeletionOfSelectedAudios()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestDeletionOfSelectedAudios() {
        GenericYesNoDialog.display(
            supportFragmentManager,
            deleteAudiosDialogText,
            this,
            dialogRequestCode
        )
    }

    private fun deselectAllAudios() {
        if (viewModel.selectedAudiosList.value?.size!! == 0) return

        viewModel.deselectAllAudios()

        // Update RecyclerView items
        (binding.rvAudios.adapter as AudioRecyclerViewAdapter).apply {
            notifyItemRangeChanged(0, this.itemCount)
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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}
