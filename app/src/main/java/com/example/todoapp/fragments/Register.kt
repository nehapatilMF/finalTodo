package com.example.todoapp.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.RegisterViewModel

class Register : Fragment() {
    private val viewModel: RegisterViewModel by activityViewModels()
    private var binding: FragmentRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.login?.setOnClickListener {

                if (NetworkUtil.isNetworkAvailable(requireContext())) {
                    findNavController().navigate(R.id.navigate_from_register_to_login)
                } else {
                    showToast(getString(R.string.no_internet_connection))
                }
                    }
        binding?.buttonNext?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val email = binding?.editTextEmail?.text.toString().trim()
                viewModel.email = email
                findNavController().navigate(R.id.navigate_from_register_to_otp)
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }
    private fun validateInputs(emailAddress: String, password: String): Boolean {
        if (emailAddress.isEmpty() || password.isEmpty()) {
            binding?.editTextEmail?.error = "Email or Password cannot be empty."
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            binding?.editTextEmail?.error = "Invalid email address"
            return false
        }
        return true
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
