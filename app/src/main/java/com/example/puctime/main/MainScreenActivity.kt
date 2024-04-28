package com.example.puctime.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.puctime.R
import com.example.puctime.access.LoginActivity
import com.example.puctime.adapter.FragmentPageAdapter
import com.example.puctime.databinding.MainScreenActivityBinding
import com.example.puctime.infra.FirebaseMethods
import com.google.android.material.tabs.TabLayout

class MainScreenActivity : AppCompatActivity() {


    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var adapter : FragmentPageAdapter

    private lateinit var binding : MainScreenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.mainActivityTablayout
        viewPager2 = binding.viewpage2

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab : TabLayout.Tab?) {
                if(tab != null){
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                }

        })

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        val backButton = binding.backButtonMainScreen
        backButton.setOnClickListener{
            signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signOut() {
        FirebaseMethods.signOutUser()
    }
}