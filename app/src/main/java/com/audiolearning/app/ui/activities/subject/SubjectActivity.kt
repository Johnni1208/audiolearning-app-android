package com.audiolearning.app.ui.activities.subject

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivitySubjectBinding
import com.audiolearning.app.util.MissingArgumentException
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubjectActivity : AppCompatActivity() {
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

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(tb_subjects)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val subjectId: Int = intent.extras?.getInt(EXTRA_SUBJECT_ID)
            ?: throw MissingArgumentException(EXTRA_SUBJECT_ID)
        MainScope().launch { viewModel.setTitleToSubjectName(subjectId) }
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
