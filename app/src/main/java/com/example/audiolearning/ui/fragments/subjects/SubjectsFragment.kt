package com.example.audiolearning.ui.fragments.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.audiolearning.R

class SubjectsFragment : Fragment() {

    private lateinit var subjectsViewModel: SubjectsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subjectsViewModel =
            ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_subjects, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        subjectsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}