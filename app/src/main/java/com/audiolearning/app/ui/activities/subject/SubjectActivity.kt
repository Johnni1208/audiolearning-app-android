package com.audiolearning.app.ui.activities.subject

import android.graphics.Typeface
import android.os.Bundle
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
        const val EXTRA_TRANSITION_NAME = "transition_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subject)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(tb_subjects)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.tvSubjectsTitle.typeface = Typeface.DEFAULT_BOLD

        val subjectId: Int = intent.extras?.getInt(EXTRA_SUBJECT_ID)
            ?: throw MissingArgumentException(EXTRA_SUBJECT_ID)
        MainScope().launch { viewModel.setTitleToSubjectName(subjectId) }

        val transitionName: String = intent.extras?.getString(EXTRA_TRANSITION_NAME)
            ?: throw MissingArgumentException(EXTRA_TRANSITION_NAME)
        viewModel.setTransitionName(transitionName)
    }
}
