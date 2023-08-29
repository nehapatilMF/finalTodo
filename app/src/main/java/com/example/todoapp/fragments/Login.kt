package com.example.todoapp.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.util.NetworkUtil

class Login : Fragment() {
    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding?.buttonlogin?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_from_login_to_todoMain)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding?.signup?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_from_login_to_register)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val email = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()
        return binding?.root
    }

    private fun validateInputs(emailAddress: String, password: String): Boolean {

        if (emailAddress.isEmpty() || password.isEmpty()) {

            binding?.etEmail?.error = "Email or Password cannot be empty."

            return false

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            binding?.etEmail?.error ="Invalid email address"
            return false
        }
        return true
    }
override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

