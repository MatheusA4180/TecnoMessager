package com.example.tecnomessager.intro.fragments

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.UserApp
import com.example.tecnomessager.databinding.FragmentOnboardingBinding
import com.example.tecnomessager.intro.viewmodel.OnboardingViewModel
import com.example.tecnomessager.utils.extension.snackBar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class OnboardingFragment : Fragment() {

    private val binding: FragmentOnboardingBinding by lazy {
        FragmentOnboardingBinding.inflate(layoutInflater)
    }
    private val viewModel: OnboardingViewModel by viewModel()
    private val arguments: OnboardingFragmentArgs by navArgs()
    private var uriPhotoSelected: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    private val getContent = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { photoSelected: Uri? ->
        if (photoSelected != null) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, photoSelected)
            val bitmapImage = ImageDecoder.decodeBitmap(source)
            binding.imageviewUserPhoto.setImageBitmap(bitmapImage)
            uriPhotoSelected = photoSelected
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
            viewModel.onProfilerEnter(
                UserApp(
                    uidUser = arguments.uidUser,
                    email = null,
                    imageProfile = uriPhotoSelected.toString(),
                    nameProfile = binding.nameProfile.text.toString(),
                    messageProfile = binding.editMessage.text.toString(),
                    contacts = null
                )
            ).observe(viewLifecycleOwner, {
                it?.let { resource ->
                    if (resource.dado) {
                        findNavController().navigate(R.id.action_onboardingFragment_to_homeActivity)
                    } else {
                        when (resource.erro) {
                            getString(R.string.name_and_message_error) -> {
                                binding.nameProfileLayout.error = getString(R.string.name_profile_error)
                                binding.editMessageLayout.error = getString(R.string.message_error)
                                    getString(R.string.empty_password)
                            }
                            getString(R.string.name_profile_error) -> {
                                binding.nameProfileLayout.error = resource.erro
                            }
                            getString(R.string.message_error) -> {
                                binding.editMessageLayout.error = resource.erro
                            }
                            else -> {
                                val messageError = resource.erro ?: getString(R.string.error_auth)
                                view.snackBar(messageError)
                            }
                        }
                    }
                }
            })
        }

        binding.nameProfile.addTextChangedListener {
            binding.nameProfileLayout.error = ""
        }

        binding.editMessage.addTextChangedListener {
            binding.editMessageLayout.error = ""
        }

    }

}
