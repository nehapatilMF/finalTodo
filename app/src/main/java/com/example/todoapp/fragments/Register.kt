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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.base64.Base64
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.RegisterViewModel

class Register : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_intro)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_intro)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setupTextChangeListeners()
        binding?.login?.setOnClickListener {
            handleLogin()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding?.buttonNext?.setOnClickListener {
            val userName = binding?.editTextUserName?.text.toString()
            val mobileNumber = binding?.editTextMobileNumber?.text.toString()
            val email = binding?.editTextEmail?.text.toString()
            Constants.userEmail = email
            val password  = binding?.editTextPassword?.text.toString()
            val confirmPassword = binding?.editTextConfirmPassword?.text.toString()
            when {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> showErrorDialog(getString(R.string.no_internet_connection))
                !ValidPatterns.isValidEmail(email) ->  {
                    val message = "Invalid or empty email id. "
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }
                !ValidPatterns.isValidNumber(mobileNumber) -> {
                    val message = "Invalid or mobile. "
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }
                !ValidPatterns.isValidPassword(password) -> {
                    val message = "Invalid or empty password. "
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> binding?.editTextPassword?.error = getString(R.string.no_match)
                else -> {

                    val encodedPassword = Base64.encodeToBase64(password)
                    val mobile = mobileNumber.toLong()
                    viewModel.signup(userName, mobile, email, encodedPassword)
                }
            }
        }
                     viewModel.signupResult.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                Toast.makeText(requireContext(),"Please check email for OTP.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.navigate_from_register_to_otp )
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }
        }
         viewModel.otpResult.observe(viewLifecycleOwner){ otp ->
             val otp1  =otp.toString()
             Constants.userOtp = otp1
         }
        return binding?.root
    }


    private fun showErrorDialog(message: String) {
        DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
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
            findNavController().navigate(R.id.navigate_from_register_to_login,)
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
