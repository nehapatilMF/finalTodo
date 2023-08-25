package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentIntroBinding

class Intro : Fragment() {
    private var binding : FragmentIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentIntroBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding?.buttonLogIn?.setOnClickListener {
                findNavController().navigate(R.id.navigate_to_login)
            }
                    binding?.buttonRegister?.setOnClickListener{
                findNavController().navigate(R.id.navigate_to_register)
            }
           return binding?.root
    }
}