package com.example.tecnomessager.intro.fragments

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tecnomessager.databinding.FragmentOnboardingBinding
import org.koin.android.ext.android.bind

class OnboardingFragment : Fragment() {

    private val binding: FragmentOnboardingBinding by lazy {
        FragmentOnboardingBinding.inflate(layoutInflater)
    }
    private var uriPhotoSelected: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    private val getContent = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { photoSelected: Uri? ->
        if (photoSelected != null) {
            uriPhotoSelected = photoSelected
            val source = ImageDecoder.createSource(requireActivity().contentResolver, photoSelected)
            val bitmapImage = ImageDecoder.decodeBitmap(source)
            binding.imageviewUserPhoto.visibility = View.VISIBLE
            binding.imageviewUserPhoto.setImageBitmap(bitmapImage)
            //binding.btnUserPhoto.visibility = View.INVISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarOnboarding.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageviewUserPhoto.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.profileEnter.setOnClickListener {
            //viewModel.onProfilerEnter()
        }

    }

}
