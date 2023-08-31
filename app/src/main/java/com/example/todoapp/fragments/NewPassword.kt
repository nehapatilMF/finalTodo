package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewPasswordBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns

class NewPassword : Fragment() {
    private var binding : FragmentNewPasswordBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.navigate_from__newPassword_to_forgotPasswordOtp)
        }
        binding?.btnNext?.setOnClickListener {
            val password  = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                if(password.isNotEmpty() && ValidPatterns.isValidPassword(password)){
                    if(confirmPassword.isNotEmpty() && ValidPatterns.isValidPassword(confirmPassword)){
                        val result = password.compareTo(confirmPassword)
                        if(result == 0 ){
                            findNavController().navigate(R.id.navigate_from__newPassword_to_login)
                        }else{
                            binding?.etNewPassword?.error = getString(R.string.no_match)
                        }
                    } else {
                        Toast.makeText(requireContext(),
                            getString(R.string.required_fields_are_empty),
                            Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),
                        getString(R.string.required_fields_are_empty),
                        Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun setupTextChangeListeners() {
        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etNewPassword?.error =
                        getString(R.string.password_pattern_requirement)
                } else {
                    binding?.etNewPassword?.error = null // Clear error message
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
        // Inflate the layout for this fragment

        binding = FragmentNewPasswordBinding.inflate(layoutInflater,container,false)
        val newPassword = binding?.etNewPassword?.text.toString()
        val confirmNewPassword = binding?.etConfirmPassword?.text.toString()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        if(newPassword.isBlank()){
            binding?.etNewPassword?.error = getString(R.string.password_is_required)
        }

        if(confirmNewPassword.isBlank()){
            binding?.etConfirmPassword?.error =
                getString(R.string.confirm_password_is_required)
        }
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}