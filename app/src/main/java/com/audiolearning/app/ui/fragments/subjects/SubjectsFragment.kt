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
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SubjectsFragment : DaggerFragment() {
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

        val subjectsAdapter =
            SubjectsRecyclerViewAdapter(viewModel.getSubjects().value ?: emptyList())
        viewModel.getSubjects().observe(viewLifecycleOwner, Observer { subjects: List<Subject> ->
            subjectsAdapter.setData(subjects)

            if (subjects.isEmpty()) binding.tvNoSubjects.show()
            else binding.tvNoSubjects.hide()
        })

        binding.rvSubjects.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = subjectsAdapter
        }

        return binding.root
    }
}