package com.example.tecnomessager.home.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnomessager.R
import com.example.tecnomessager.databinding.ActivityHomeBinding
import com.example.tecnomessager.home.adapter.HomeViewPagerAdapter
import com.example.tecnomessager.home.viewmodel.HomeActivityViewModel
import com.example.tecnomessager.intro.activity.IntroActivity
import com.example.tecnomessager.intro.viewmodel.SplashViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val viewModel: HomeActivityViewModel by viewModel()

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

        binding.toolbarHome.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search_bar -> {
                    true
                }
                R.id.logout -> {
                    viewModel.logoutApp()
                    startActivity(Intent(this, IntroActivity::class.java))
                    true
                }
                R.id.config_options -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

    }
}