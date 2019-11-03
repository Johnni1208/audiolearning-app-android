package com.example.audiolearning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.audiolearning.fragments.about_us.AboutUsFragment
import com.example.audiolearning.fragments.recorder.RecorderFragment
import com.example.audiolearning.fragments.subjects.SubjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragments = arrayOf(AboutUsFragment(), RecorderFragment(), SubjectsFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val pager = findViewById<ViewPager>(R.id.pager)


        val pageAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        pager.adapter = pageAdapter
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
    }
}
