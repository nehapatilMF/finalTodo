package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSplashBinding

class Splash : Fragment() {
    private var binding : FragmentSplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                // Animation started
            }
            override fun onAnimationEnd(animation: Animation?) {
                findNavController().navigate(R.id.navigate_to_intro)
                       }
            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
                binding?.splashScreen?.clearAnimation() // Clear the animation to stop it
            }
        })
        return binding?.root
    }
}