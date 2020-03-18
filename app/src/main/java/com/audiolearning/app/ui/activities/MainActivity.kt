package com.audiolearning.app.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivityMainBinding
import com.audiolearning.app.ui.fragments.about_us.AboutUsFragment
import com.audiolearning.app.ui.fragments.recorder.RecorderFragment
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        private lateinit var menuItemDelete: MenuItem
        fun showDeleteIcon() {
            if (!menuItemDelete.isVisible) menuItemDelete.isVisible = true
        }

        fun hideDeleteIcon() {
            if (menuItemDelete.isVisible) menuItemDelete.isVisible = false
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        menu?.let { menuItemDelete = it.findItem(R.id.menu_item_delete) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_delete -> {
                subjectsFragment.requestDeletionOfSubjects()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}