package com.example.tecnomessager.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tecnomessager.home.features.camera.fragments.CameraFragment
import com.example.tecnomessager.home.features.notification.fragments.NotificationFragment
import com.example.tecnomessager.home.features.status.fragments.StatusFragment
import com.example.tecnomessager.home.fragments.ListContactsFragment

class HomeViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = NUMBER_OF_PAGES

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CameraFragment()
            1 -> ListContactsFragment()
            2 -> StatusFragment()
            else -> NotificationFragment()
        }
    }
    companion object{
        const val NUMBER_OF_PAGES = 4
    }
}