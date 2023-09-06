package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.base64.Base64
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.util.DialogUtils
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


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding?.login?.setOnClickListener {
            handleLogin()
        }
        binding?.buttonNext?.setOnClickListener {
            handleRegister()
        }
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        viewModel.signupResult.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                findNavController().navigate(R.id.navigate_from_register_to_otp)
            }else{
                val message = getString(R.string.aready_user_exist)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
        return binding?.root
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

            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
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
                                val encodedPassword = Base64.encodeToBase64(password)
                                val mobile = mobileNumber.toLong()
                                viewModel.signup(userName, mobile, email, encodedPassword)

                            }else{
                                binding?.editTextPassword?.error = getString(R.string.no_match)
                            }
                        }else{
                            val message = getString(R.string.required_fields_are_empty)
                            DialogUtils.showAutoDismissAlertDialog(requireContext(), message) }
                    }else{
                        val message = getString(R.string.required_fields_are_empty)
                        DialogUtils.showAutoDismissAlertDialog(requireContext(), message)}
                }else{
                    val message = getString(R.string.required_fields_are_empty)
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)}
            }else{
                val message = getString(R.string.required_fields_are_empty)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)}
        } else {
            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}
