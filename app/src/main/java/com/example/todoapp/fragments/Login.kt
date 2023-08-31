package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import java.util.regex.Pattern


class Login : Fragment() {
    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextChangeListeners()

       val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_intro)
       }

        binding?.forgotPassword?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_from_login_to_forgotPassword)
            }    else {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        }
        binding?.buttonLogin?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val email = binding?.etEmail?.text.toString()
                val password = binding?.etPassword?.text.toString()

                if (ValidPatterns.isValidEmail(email) && ValidPatterns.isValidPassword(password)) {
                    // Navigate to the next screen if both email and password are valid
                    findNavController().navigate(R.id.navigate_from_login_to_todoMain)
                }else{
                    Toast.makeText(requireContext(),
                        getString(R.string.required_fields_are_empty),
                    Toast.LENGTH_SHORT).show()

                }
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

    private fun setupTextChangeListeners() {
        binding?.etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    binding?.etEmail?.error = getString(R.string.invalid_or_empty_email_id)
                } else {
                    binding?.etEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })

        binding?.etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etPassword?.error = getString(R.string.password_pattern_requirement)
                } else {
                    binding?.etPassword?.error = null // Clear error message
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)


        val email = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()
        if(email.isEmpty()){
            binding?.etEmail?.error = getString(R.string.email_is_required)
        }
        if(password.isBlank()){
            binding?.etPassword?.error = getString(R.string.invalid_or_empty_password)
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
