package com.audiolearning.app.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.databinding.FragmentHomeBinding
import com.audiolearning.app.extension.hide
import com.audiolearning.app.extension.show
import com.audiolearning.app.ui.fragment.BackPressableFragment
import com.audiolearning.app.ui.fragment.pager.aboutus.AboutUsFragment
import com.audiolearning.app.ui.fragment.pager.recorder.RecorderPagerFragment
import com.audiolearning.app.ui.fragment.pager.subjects.SubjectsPagerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BackPressableFragment(), HomeToolBarChangeListener {
    val viewModel: HomeFragmentViewModel by viewModels()

    private val subjectsFragment = SubjectsPagerFragment(this)
    private val fragments: Array<Fragment> = arrayOf(
        AboutUsFragment(),
        RecorderPagerFragment(),
        subjectsFragment
    )

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.navView.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener())

        binding.pager.apply {
            adapter = ScreenSlidePagerAdapter(childFragmentManager)
            registerOnPageChangeCallback(OnPageChangeCallback())
            setCurrentItem(viewModel.previousFragmentPosition, false)
        }

        setupToolbars()

        return binding.root
    }

    private inner class OnNavigationItemSelectedListener :
        BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(selectedItem: MenuItem): Boolean {
            when (selectedItem.itemId) {
                R.id.navigation_about_us -> binding.pager.currentItem = POSITION_ABOUT_US_FRAGMENT
                R.id.navigation_recorder -> binding.pager.currentItem = POSITION_RECORDER_FRAGMENT
                R.id.navigation_subjects -> binding.pager.currentItem = POSITION_SUBJECT_FRAGMENT
                else -> return false
            }

            return true
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    private inner class OnPageChangeCallback :
        ViewPager2.OnPageChangeCallback() {
        private lateinit var previousMenuItem: MenuItem

        override fun onPageSelected(position: Int) {
            if (::previousMenuItem.isInitialized) {
                previousMenuItem.isChecked = false
            } else {
                binding.navView.menu.getItem(0).isChecked = false
            }

            binding.navView.menu.getItem(position).isChecked = true
            previousMenuItem = binding.navView.menu.getItem(position)

            viewModel.previousFragmentPosition = position

            changeTitleOfToolBar(position)
        }
    }

    private fun changeTitleOfToolBar(position: Int) {
        when (position) {
            POSITION_ABOUT_US_FRAGMENT -> viewModel.title.value = getString(R.string.title_about_us)
            POSITION_RECORDER_FRAGMENT -> viewModel.title.value = getString(R.string.title_recorder)
            POSITION_SUBJECT_FRAGMENT -> viewModel.title.value = getString(R.string.title_subjects)
            else -> throw IllegalStateException("No title for position: $position")
        }
    }

    private fun setupToolbars() {
        binding.tbMain.title = ""

        binding.tbMainSelectedSubjects.setNavigationOnClickListener {
            subjectsFragment.deselectAllSubjects()
            binding.tbMainSelectedSubjects.hide()
            binding.tbMain.show()
        }

        binding.tbMainSelectedSubjects.inflateMenu(R.menu.delete_menu)
        binding.tbMainSelectedSubjects.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_item_delete -> {
                    subjectsFragment.requestDeletionOfSelectedSubjects()
                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
    }

    override fun onSelectedSubjectsChange(selectedSubjectsList: ArrayList<Subject>) {
        if (selectedSubjectsList.isEmpty()) {
            binding.tbMain.show()
            binding.tbMainSelectedSubjects.hide()
            return
        }

        binding.tbMain.hide()
        binding.tbMainSelectedSubjects.show()

        binding.tbMainSelectedSubjects.title = selectedSubjectsList.size.toString()
    }

    override fun onBackPressed() {
        if (binding.pager.currentItem != POSITION_RECORDER_FRAGMENT)
            binding.pager.currentItem = POSITION_RECORDER_FRAGMENT
    }
}

/**
 * Interface for children of Activities to implement. They then can call the listener to change
 * ToolBar of the parent
 */
interface HomeToolBarChangeListener {
    fun onSelectedSubjectsChange(selectedSubjectsList: ArrayList<Subject>)
}
