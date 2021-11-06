package com.example.tecnomessager.intro.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tecnomessager.R
import com.example.tecnomessager.databinding.FragmentSplashBinding
import com.example.tecnomessager.intro.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private val binding: FragmentSplashBinding by lazy {
        FragmentSplashBinding.inflate(layoutInflater)
    }
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                viewModel.isLoggedIn().observe(viewLifecycleOwner,{ isLoggedIn ->
                    if(isLoggedIn){
                        findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
                    }else{
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                })
            },
            SPLASH_DELAY
        )

    }

    companion object {
        private const val SPLASH_DELAY = 3000L
    }

}
