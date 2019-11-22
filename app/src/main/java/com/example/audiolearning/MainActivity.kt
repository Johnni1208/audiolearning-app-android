package com.example.audiolearning

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.audiolearning.databinding.ActivityMainBinding
import com.example.audiolearning.fragments.about_us.AboutUsFragment
import com.example.audiolearning.fragments.recorder.RecorderFragment
import com.example.audiolearning.fragments.subjects.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragments = arrayOf(AboutUsFragment(), RecorderFragment(), SubjectsFragment())

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.navView.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener())

        binding.pager.apply {
            adapter = ScreenSlidePagerAdapter(supportFragmentManager)
            addOnPageChangeListener(OnPageChangeListener())
        }
    }

    private inner class OnNavigationItemSelectedListener :
        BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(selectedItem: MenuItem): Boolean {
            val pager = binding.pager
            when (selectedItem.itemId) {
                R.id.navigation_about_us -> {
                    pager.currentItem = 0
                    return true
                }

                R.id.navigation_recorder -> {
                    pager.currentItem = 1
                    return true
                }

                R.id.navigation_subjects -> {
                    pager.currentItem = 2
                    return true
                }

                else -> return false
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
    }

    private inner class OnPageChangeListener :
        ViewPager.OnPageChangeListener {
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

        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }
    }

}
