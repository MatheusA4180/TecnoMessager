package com.example.tecnomessager.intro.fragments

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
import com.example.tecnomessager.databinding.FragmentLoginBinding
import com.example.tecnomessager.intro.viewmodel.LoginViewModel
import com.example.tecnomessager.utils.extension.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginCreate.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginEnter.setOnClickListener {
            binding.progressCircular.isVisible = true
            viewModel.auth(
                User(
                    binding.loginEmail.text.toString(),
                    binding.loginPassword.text.toString()
                )
            ).observe(viewLifecycleOwner, {
                    it?.let { resource ->
                        if (resource.dado) {
                            findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
                        } else {
                            when(resource.erro){
                                getString(R.string.empty_email_and_password) -> {
                                    binding.loginUsernameLayout.error = getString(R.string.empty_email)
                                    binding.loginPasswordLayout.error = getString(R.string.empty_password)
                                }
                                getString(R.string.empty_email) -> {
                                    binding.loginUsernameLayout.error = resource.erro
                                }
                                getString(R.string.empty_password) -> {
                                    binding.loginPasswordLayout.error = resource.erro
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

        binding.loginEmail.addTextChangedListener {
            binding.loginUsernameLayout.error = ""
        }

        binding.loginPassword.addTextChangedListener {
            binding.loginPasswordLayout.error = ""
        }

    }

}