package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRegisterBinding

class Register : Fragment() {
    private var binding : FragmentRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding?.login?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_register_to_login)
        }
        binding?.buttonNext?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_register_to_otp)
        }
        return binding?.root
    }
}