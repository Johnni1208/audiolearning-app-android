package com.audiolearning.app.ui.activity.audiosofsubject

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.audiolearning.app.R
import com.audiolearning.app.adapter.AdapterDataEvent
import com.audiolearning.app.adapter.recycler.selectable.AudiosRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.databinding.ActivityAudiosOfSubjectBinding
import com.audiolearning.app.exception.MissingArgumentException
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import com.audiolearning.app.ui.dialog.genericyesno.DialogDataReceiver
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialog
import com.audiolearning.app.ui.dialog.genericyesno.GenericYesNoDialogTexts
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AudiosOfSubjectActivity : AppCompatActivity(),
    ItemSelectListener<Audio>,
    DialogDataReceiver {
    private var dialogRequestCode: Int = 0 // lateinit

    private val viewModel: AudiosOfSubjectActivityViewModel by viewModels()
    private lateinit var binding: ActivityAudiosOfSubjectBinding

    companion object {
        const val EXTRA_SUBJECT_ID = "extra_subject_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_audios_of_subject)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        dialogRequestCode =
            resources.getInteger(R.integer.request_code_audiosOfSubjectsActivity_delete_audios)

        setupSubject()
        setupToolbar()
        setupEmptyStateMessage()
        setupRecyclerView()
    }

    private fun setupSubject() = runBlocking {
        viewModel.setSubject(
            intent.extras?.getInt(EXTRA_SUBJECT_ID)
                ?: throw MissingArgumentException(
                    EXTRA_SUBJECT_ID
                )
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbAudiosOfSubject)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.selectedAudiosList.observe(this, Observer { list ->
            if (list.isNotEmpty()) binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_cross_24dp)
            else binding.tbAudiosOfSubject.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24dp)
        })
    }

    private fun setupEmptyStateMessage() {
        viewModel.audios.observe(this, Observer { audios: List<Audio> ->
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
        viewModel.audios.observe(this, Observer { audios: List<Audio> ->
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
                } else onBackPressed()
            }

            R.id.menu_item_delete -> requestDeletionOfSelectedAudios()
        }
        return super.onOptionsItemSelected(item)
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
            supportFragmentManager,
            deleteAudiosDialogText,
            this,
            dialogRequestCode
        )
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
