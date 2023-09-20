package com.example.todoapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentSplashBinding

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
                if (sessionManager.getAccessToken()?.isNotBlank() == true) {
                    findNavController().navigate(R.id.navigate_to_home)

                    Constants.accessToken = sessionManager.getAccessToken().toString()
                } else {
                    findNavController().navigate(R.id.navigate_to_intro)
                    Constants.clearAccessToken()
                    sessionManager.clearTokens()
                }
            }
            override fun onAnimationRepeat(animation: Animation?) {
                binding?.splashScreen?.clearAnimation() // Clear the animation to stop it
            }
        })
        return binding?.root
    }



    }
