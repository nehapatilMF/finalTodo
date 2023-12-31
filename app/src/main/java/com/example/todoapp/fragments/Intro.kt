package com.example.todoapp.fragments

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentIntroBinding

@Suppress("DEPRECATION")
class Intro : Fragment() {
    private var binding : FragmentIntroBinding? = null
    @SuppressLint("ObsoleteSdkInt")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SDK_INT >= VERSION_CODES.ECLAIR) {
            requireActivity().window.statusBarColor = resources.getColor(R.color.bckcolor)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.buttonLogIn?.setOnClickListener {
            findNavController().navigate(R.id.action_intro_to_login)
        }
        binding?.buttonRegister?.setOnClickListener{
            findNavController().navigate(R.id.action_intro_to_register)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}