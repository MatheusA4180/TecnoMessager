package com.example.tecnomessager.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnomessager.R
import com.example.tecnomessager.databinding.ActivityHomeBinding
import com.example.tecnomessager.home.adapter.HomeViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.viewPagerHome.adapter = HomeViewPagerAdapter(this)
        TabLayoutMediator(
            binding.tabsMenu,
            binding.viewPagerHome
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = resources.getDrawable(R.drawable.ic_camera)
                }
                1 -> {
                    tab.text = "CONVERSAS"
                }
                2 -> {
                    tab.text = "STATUS"
                }
                3 -> {
                    tab.text = "NOTIFICAÇÃO"
                }
            }
        }.attach()

    }
}