package com.example.todoapp.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentSplashBinding
import com.example.todoapp.viewModels.RefreshTokenViewModel

@Suppress("DEPRECATION")
class Splash : Fragment() {
    private var binding : FragmentSplashBinding? = null


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.backcolor)
        }
        binding = FragmentSplashBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.logo_move_animation)
        binding?.splashScreen?.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                val sessionManager = SessionManager(requireContext())
                if (sessionManager.isLoggedIn() && sessionManager.isAccessTokenExpired()) {
                    refreshToken()
                } else {
                    proceedToNextScreen()
                }
            }
            override fun onAnimationRepeat(animation: Animation?) {
                binding?.splashScreen?.clearAnimation() // Clear the animation to stop it
            }
        })
        return binding?.root
    }

    private fun refreshToken() {
        val handler = Handler()
        handler.postDelayed({
        val viewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
          val refreshToken = SessionManager(requireContext()).getRefreshToken().toString()

          viewModel.refreshToken(refreshToken)
        val sessionManager = SessionManager(requireContext())
        viewModel.result.observe(viewLifecycleOwner){ status ->
            if(status == "true"){
                sessionManager.clearTokens()
                Constants.clearAccessToken()
                viewModel.getAuthTokens().observe(viewLifecycleOwner){ authTokens ->
                    val accessToken = authTokens.accessToken
                    Constants.accessToken = accessToken
                    val refreshToken = authTokens.refreshToken
                    Constants.refreshToken = refreshToken
                    sessionManager.saveTokens(accessToken,refreshToken)
                }
            }
            else{
                Toast.makeText(requireContext(),status.toString(),Toast.LENGTH_SHORT).show()
            }
        }
            proceedToNextScreen()
        }, 2000) // Simulating a delay for the token refresh request
    }

    private fun proceedToNextScreen() {
        if (SessionManager(requireContext()).isLoggedIn()) {
            navigateToMainScreen()
        } else {
            navigateToLoginScreen()
        }
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.navigate_to_todoMain)
        Constants.accessToken = SessionManager(requireContext()).getAccessToken()
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(R.id.navigate_to_intro)
    }
}
