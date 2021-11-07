package com.example.tecnomessager.home.features.camera.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tecnomessager.databinding.FragmentCameraBinding
import com.example.tecnomessager.databinding.FragmentListContactsBinding
import com.example.tecnomessager.home.features.contacts.adapter.ListContactsAdapter
import com.example.tecnomessager.home.features.contacts.viewmodel.ListContactsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CameraFragment : Fragment() {

    private val binding: FragmentCameraBinding by lazy {
        FragmentCameraBinding.inflate(layoutInflater)
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