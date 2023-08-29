package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        setupTextChangeListeners()
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun setupTextChangeListeners() {
        binding?.editTextMobileNumber?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.editTextMobileNumber?.error = "Mobile number required"
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mobileNumber = s.toString()
                if (!isValidNumber(mobileNumber)) {
                    binding?.editTextMobileNumber?.error = "Invalid mobile number"
                } else {
                    binding?.editTextMobileNumber?.error = null // Clear error message
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding?.editTextEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!isValidEmail(email)) {
                    binding?.editTextEmail?.error = getString(R.string.invalid_or_empty_email_id)
                } else {
                    binding?.editTextEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })

        binding?.editTextPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!isValidPassword(password)) {
                    binding?.editTextPassword?.error = "Password should be at least 6 characters long"
                } else {
                    binding?.editTextPassword?.error = null // Clear error message
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }

    // Define your email and password validation methods
    private fun isValidEmail(email: String): Boolean {
        // You can use a regular expression or other validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Add your password validation logic here (e.g., minimum length)
        return password.length >= 6
    }
    private fun isValidNumber(phoneNumber : String):Boolean{
        val pattern = Regex("^\\d{10}$")

        // Use the matches() function to check if the input matches the pattern.
        return pattern.matches(phoneNumber)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val email = binding?.editTextEmail?.text.toString()
        val password = binding?.editTextPassword?.text.toString()
        val username = binding?.editTextUserName?.text.toString()
        val mobileNumber = binding?.editTextMobileNumber?.text.toString()
        if(email.isEmpty()){
            binding?.editTextEmail?.error = "Email is Required"
        }
        if(password.isBlank()){
            binding?.editTextPassword?.error = "Password is required"
        }
        if(username.isBlank()){
            binding?.editTextUserName?.error ="Required field"
        }
        if(mobileNumber.isBlank()){
            binding?.editTextMobileNumber?.error = "Required field"
        }
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
