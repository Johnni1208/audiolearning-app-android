package com.audiolearning.app.ui.fragment.pager.aboutus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.audiolearning.app.R

class AboutUsFragment : Fragment() {

    private lateinit var aboutUsViewModel: AboutUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutUsViewModel =
            ViewModelProvider(this).get(AboutUsViewModel::class.java)
        val root = inflater.inflate(R.layout.pager_fragment_about_us, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        aboutUsViewModel.text.observe(viewLifecycleOwner, { textView.text = it })
        return root
    }
}
