package com.audiolearning.app.ui.activities.subject

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
import com.audiolearning.app.databinding.ActivitySubjectBinding
import com.audiolearning.app.util.MissingArgumentException
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SubjectActivity : AppCompatActivity(), ItemSelectListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SubjectActivityViewModel> { viewModelFactory }

    private lateinit var binding: ActivitySubjectBinding

    companion object {
        const val EXTRA_SUBJECT_ID = "extra_subject_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        runBlocking {
            viewModel.setSubject(
                intent.extras?.getInt(EXTRA_SUBJECT_ID)
                    ?: throw MissingArgumentException(EXTRA_SUBJECT_ID)
            )
        }
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(tb_subjects)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
//        viewModel.selectedSubjectsList.observe(
//            viewLifecycleOwner,
//            Observer { selectedSubjectsList: ArrayList<Subject> ->
//                audioAdapter.isSelecting = selectedSubjectsList.isNotEmpty()
//            })

        binding.rvAudios.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = audioAdapter
        }
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

    override fun onItemDeselect(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemSelect(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onItemClick(id: Int) {
        TODO("Not yet implemented")
    }
}
