package com.audiolearning.app.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.databinding.ActivityMainBinding
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import com.audiolearning.app.ui.fragments.about_us.AboutUsFragment
import com.audiolearning.app.ui.fragments.recorder.RecorderFragment
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityToolBarChangeListener {
    private val subjectsFragment = SubjectsFragment(this)
    private val fragments: Array<Fragment> = arrayOf(
        AboutUsFragment(),
        RecorderFragment(),
        subjectsFragment
    )
    private val POSITION_ABOUT_US_FRAGMENT = 0
    private val POSITION_RECORDER_FRAGMENT = 1
    private val POSITION_SUBJECT_FRAGMENT = 2

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        binding.navView.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener())

        binding.pager.apply {
            adapter = ScreenSlidePagerAdapter(supportFragmentManager)
            registerOnPageChangeCallback(OnPageChangeCallback())
            setCurrentItem(POSITION_RECORDER_FRAGMENT, false)
        }

        setupToolbars()
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
        private var previousPosition: Int = -1

        override fun onPageSelected(position: Int) {
            if (::previousMenuItem.isInitialized) {
                previousMenuItem.isChecked = false
            } else {
                binding.navView.menu.getItem(0).isChecked = false
            }

            binding.navView.menu.getItem(position).isChecked = true
            previousMenuItem = binding.navView.menu.getItem(position)

            if (previousPosition == POSITION_SUBJECT_FRAGMENT) subjectsFragment.deselectAllSubjects()
            previousPosition = position

            changeTitleOfToolBar(position)
        }
    }

    private fun changeTitleOfToolBar(position: Int) {
        when (position) {
            POSITION_ABOUT_US_FRAGMENT -> tb_main.setTitle(R.string.title_about_us)
            POSITION_RECORDER_FRAGMENT -> tb_main.setTitle(R.string.title_recorder)
            POSITION_SUBJECT_FRAGMENT -> tb_main.setTitle(R.string.title_subjects)
            else -> throw IllegalStateException("No title for position: $position")
        }
    }

    private fun setupToolbars() {
        setSupportActionBar(binding.tbMain)

        binding.tbMainSelectedSubjects.setNavigationOnClickListener {
            subjectsFragment.deselectAllSubjects()
            binding.tbMainSelectedSubjects.hide()
            binding.tbMain.show()
        }

        binding.tbMainSelectedSubjects.inflateMenu(R.menu.delete_selected_subjects_menu)
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
}

/**
 * Interface for children of Activities to implement. They then can call the listener to change
 * ToolBar of the parent
 */
interface MainActivityToolBarChangeListener {
    fun onSelectedSubjectsChange(selectedSubjectsList: ArrayList<Subject>)
}