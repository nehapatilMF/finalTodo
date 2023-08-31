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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.RegisterViewModel

class Register : Fragment() {
    private val viewModel: RegisterViewModel by activityViewModels()
    private var binding: FragmentRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_intro)
        }
        setupTextChangeListeners()

        binding?.login?.setOnClickListener {
            handleLogin()
        }
        binding?.buttonNext?.setOnClickListener {
            handleRegister()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        handleEmptyField()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        return binding?.root
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun handleEmptyField(){
        val username = binding?.editTextUserName?.text.toString()
        val mobileNumber = binding?.editTextMobileNumber?.text.toString()
        val email = binding?.editTextEmail?.text.toString()
        val password  = binding?.editTextPassword?.text.toString()
        val confirmPassword = binding?.editTextConfirmPassword?.text.toString()

        if(email.isEmpty()){
            binding?.editTextEmail?.error = getString(R.string.email_is_required)
        }
        if(password.isBlank()){
            binding?.editTextPassword?.error = getString(R.string.password_is_required)
        }
        if(confirmPassword.isBlank()){
            binding?.editTextConfirmPassword?.error =
                getString(R.string.confirm_password_is_required)
        }
        if(username.isBlank()){
            binding?.editTextUserName?.error = getString(R.string.username_is_required)
        }
        if(mobileNumber.isBlank()){
            binding?.editTextMobileNumber?.error = getString(R.string.mobile_number_is_required)
        }
    }
    private fun setupTextChangeListeners() {
        binding?.editTextMobileNumber?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.editTextMobileNumber?.error = getString(R.string.mobile_number_required)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mobileNumber = s.toString()
                if (!ValidPatterns.isValidNumber(mobileNumber)) {
                    binding?.editTextMobileNumber?.error = getString(R.string.invalid_mobile_number)
                } else {
                    binding?.editTextMobileNumber?.error = null // Clear error message
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding?.editTextEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    binding?.editTextEmail?.error = getString(R.string.invalid_or_empty_email_id)
                } else {
                    binding?.editTextEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding?.editTextPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.editTextPassword?.error =
                        getString(R.string.password_pattern_requirement)
                } else {
                    binding?.editTextPassword?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    private fun handleLogin() {
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            findNavController().navigate(R.id.navigate_from_register_to_login)
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }
    private fun handleRegister(){
        val userName = binding?.editTextUserName?.text.toString()
        val mobileNumber = binding?.editTextMobileNumber?.text.toString()
        val email = binding?.editTextEmail?.text.toString()
        val password  = binding?.editTextPassword?.text.toString()
        val confirmPassword = binding?.editTextConfirmPassword?.text.toString()
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            if(userName.isNotEmpty()){
                if(ValidPatterns.isValidEmail(email) && email.isNotEmpty()){
                    if(ValidPatterns.isValidNumber(mobileNumber) && mobileNumber.isNotEmpty()){
                        if(ValidPatterns.isValidPassword(password) && password.isNotEmpty()){
                            val result = password.compareTo(confirmPassword)
                            if(result == 0 ){
                                viewModel.email = email
                                findNavController().navigate(R.id.navigate_from_register_to_otp)
                            }else{
                                binding?.editTextPassword?.error = getString(R.string.no_match)
                            }
                        }else{showToast(getString(R.string.required_fields_are_empty))}
                    }else{showToast(getString(R.string.required_fields_are_empty))}
                }else{showToast(getString(R.string.required_fields_are_empty))}
            }else{showToast(getString(R.string.required_fields_are_empty))}
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
