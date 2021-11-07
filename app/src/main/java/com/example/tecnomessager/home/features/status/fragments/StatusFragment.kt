package com.example.tecnomessager.home.features.status.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tecnomessager.databinding.FragmentNotificationBinding
import com.example.tecnomessager.databinding.FragmentStatusBinding

class StatusFragment: Fragment() {

    private val binding: FragmentStatusBinding by lazy {
        FragmentStatusBinding.inflate(layoutInflater)
    }
    //private val viewModel: ListContactsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}