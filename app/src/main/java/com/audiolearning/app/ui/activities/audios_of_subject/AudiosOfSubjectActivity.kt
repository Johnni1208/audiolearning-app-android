package com.audiolearning.app.ui.activities.audios_of_subject

import android.os.Bundle
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
import com.audiolearning.app.util.MissingArgumentException
import com.google.android.material.appbar.AppBarLayout
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_audios_of_subject.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AudiosOfSubjectActivity : AppCompatActivity(), ItemSelectListener<Audio> {
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
        setSupportActionBar(tb_subjects)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}
