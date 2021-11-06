package com.example.tecnomessager.intro.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.User
import com.example.tecnomessager.databinding.FragmentRegisterBinding
import com.example.tecnomessager.intro.viewmodel.RegisterViewModel
import com.example.tecnomessager.utils.extension.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private val binding: FragmentRegisterBinding by lazy {
        FragmentRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarRegister.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.registrationCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.registrationEmail.addTextChangedListener {
            binding.registrationUsernameLayout.error = ""
        }

        binding.registrationPassword.addTextChangedListener {
            binding.registrationPasswordLayout.error = ""
        }

        binding.registrationConfirmPassword.addTextChangedListener {
            binding.registrationConfirmPasswordLayout.error = ""
        }

        binding.registrationConfirm.setOnClickListener {
            binding.progressCircular.isVisible = true
            viewModel.onRegistrationUser(
                User(
                    binding.registrationEmail.text.toString(),
                    binding.registrationPassword.text.toString()
                ),
                binding.registrationConfirmPassword.text.toString()
            ).observe(viewLifecycleOwner, {
                it?.let { resource ->
                    if (resource.dado) {
                        view.snackBar(getString(R.string.register_sucess))
                        findNavController().navigate(R.id.action_registerFragment_to_onboardingFragment)
                    } else {
                        when(resource.erro){
                            getString(R.string.empty_email_and_password_and_conf_password) -> {
                                binding.registrationUsernameLayout.error = getString(R.string.empty_email)
                                binding.registrationPasswordLayout.error = getString(R.string.empty_password)
                                binding.registrationConfirmPasswordLayout.error = getString(R.string.empty_password)
                            }
                            getString(R.string.empty_email) -> {
                                binding.registrationUsernameLayout.error = resource.erro
                            }
                            getString(R.string.empty_password) -> {
                                binding.registrationPasswordLayout.error = resource.erro
                            }
                            getString(R.string.diff_password_and_conf_password) -> {
                                binding.registrationPasswordLayout.error = getString(R.string.diff_password_and_conf_password)
                                binding.registrationConfirmPasswordLayout.error = getString(R.string.diff_password_and_conf_password)
                            }
                            else ->{
                                val messageError = resource.erro ?: getString(R.string.error_auth)
                                view.snackBar(messageError)
                            }
                        }
                    }
                }
            })
            binding.progressCircular.isVisible = false
        }

    }

}