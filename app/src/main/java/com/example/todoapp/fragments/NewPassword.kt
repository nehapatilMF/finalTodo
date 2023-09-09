package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.base64.Base64
import com.example.todoapp.databinding.FragmentNewPasswordBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.ForgotPasswordOtpViewModel
import com.example.todoapp.viewModels.ForgotPasswordViewModel
import com.example.todoapp.viewModels.NewPasswordViewModel

class NewPassword : Fragment() {
    private var binding : FragmentNewPasswordBinding? = null

  //  private val viewModel: NewPasswordViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.navigate_from_newPassword_to_forgotPasswordOtp)
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


        binding = FragmentNewPasswordBinding.inflate(layoutInflater,container,false)
        val viewModel = ViewModelProvider(this)[NewPasswordViewModel::class.java]
        val forgotPasswordViewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        val email = forgotPasswordViewModel.email
        var npOtp: String? = null
        val forgotPasswordOtpViewModel = ViewModelProvider(this)[ForgotPasswordOtpViewModel::class.java]
        forgotPasswordOtpViewModel.otp.observe(viewLifecycleOwner) { otp ->
            npOtp = otp
        }
        binding?.btnNext?.setOnClickListener {
            val password  = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                if(password.isNotEmpty() && ValidPatterns.isValidPassword(password)){
                    if(confirmPassword.isNotEmpty() && ValidPatterns.isValidPassword(confirmPassword)){
                        val result = password.compareTo(confirmPassword)
                        if(result == 0 ){
                            viewModel.email = email
                            val encodedPassword = Base64.encodeToBase64(password)
                            val otp = npOtp?.toLong()
                            if (otp != null) {
                                viewModel.resetPassword(otp,email,encodedPassword)
                            }
                        }else{
                            binding?.etNewPassword?.error = getString(R.string.no_match)
                        }
                    } else {

                        val message =  getString(R.string.required_fields_are_empty)
                        DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
                    }
                }else{

                    val message =  getString(R.string.required_fields_are_empty)
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)

                }
            }else{

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }

        }
        val newPassword = binding?.etNewPassword?.text.toString()
        val confirmNewPassword = binding?.etConfirmPassword?.text.toString()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        viewModel.resetPasswordResult.observe(viewLifecycleOwner){ status ->

            if(status == "200"){
                findNavController().navigate(R.id.navigate_from_newPassword_to_login)
            }else{

                val message = "Invalid otp"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}