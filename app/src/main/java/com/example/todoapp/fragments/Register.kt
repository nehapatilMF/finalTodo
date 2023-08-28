package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.viewModels.RegisterViewModel

class Register : Fragment() {
    private var binding : FragmentRegisterBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel: RegisterViewModel by activityViewModels()
        sharedViewModel.email = binding?.editTextEmail?.text.toString()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.login?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_register_to_login)
        }
        binding?.buttonNext?.setOnClickListener{
            sharedViewModel.email = binding?.editTextEmail?.text.toString()
            findNavController().navigate(R.id.navigate_from_register_to_otp)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }
}