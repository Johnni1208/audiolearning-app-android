package com.audiolearning.app.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class MainActivity : AppCompatActivity() {
    private val subjectsFragment = SubjectsFragment()
    private val fragments: Array<Fragment> = arrayOf(
        AboutUsFragment(),
        RecorderFragment(),
        subjectsFragment
    )
    private val recorderFragmentPosition = 1
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var mainToolbar: Toolbar
        lateinit var subjectsSelectedToolbar: Toolbar

        fun onSelectedSubjectsChanged(selectedSubjectsList: ArrayList<Subject>) {
            if (selectedSubjectsList.isEmpty()) {
                mainToolbar.show()
                subjectsSelectedToolbar.hide()
                return
            }

            mainToolbar.hide()
            subjectsSelectedToolbar.show()

            subjectsSelectedToolbar.title = selectedSubjectsList.size.toString()
        }
    }

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
            setCurrentItem(recorderFragmentPosition, false)
        }

        setupToolbars()
    }

    private inner class OnNavigationItemSelectedListener :
        BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(selectedItem: MenuItem): Boolean {
            val pager = binding.pager

            val menuItemToPagerItemMap = mapOf(
                R.id.navigation_about_us to fun() { pager.currentItem = 0 },
                R.id.navigation_recorder to fun() { pager.currentItem = 1 },
                R.id.navigation_subjects to fun() { pager.currentItem = 2 }
            )

            menuItemToPagerItemMap[selectedItem.itemId]?.invoke()
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

            changeTitleOfToolBar(position)
        }
    }

    private fun changeTitleOfToolBar(position: Int) {
        when (position) {
            0 -> tb_main.setTitle(R.string.title_about_us)
            1 -> tb_main.setTitle(R.string.title_recorder)
            2 -> tb_main.setTitle(R.string.title_subjects)
            else -> throw IllegalStateException("No title for position: $position")
        }
    }

    private fun setupToolbars() {
        mainToolbar = binding.tbMain
        subjectsSelectedToolbar = binding.tbSubjectsSelected

        setSupportActionBar(binding.tbMain)

        binding.tbSubjectsSelected.setNavigationOnClickListener {
            subjectsFragment.deselectAllSubjects()
            binding.tbSubjectsSelected.hide()
            binding.tbMain.show()
        }

        binding.tbSubjectsSelected.inflateMenu(R.menu.delete_selected_subjects_menu)
        binding.tbSubjectsSelected.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_item_delete -> {
                    subjectsFragment.requestDeletionOfSubjects()
                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
    }
}