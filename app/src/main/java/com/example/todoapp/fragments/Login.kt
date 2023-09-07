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
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.base64.Base64
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.LoginViewModel

class Login : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_intro)
        }

        setupTextChangeListeners()

        binding?.forgotPassword?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_from_login_to_forgotPassword)
            }    else {

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)

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

        val loginViewModel: LoginViewModel by activityViewModels()

        loginViewModel.getAuthTokens().observe(viewLifecycleOwner){ authTokens ->
            val accessToken = authTokens.accessToken
            Constants.accessToken = accessToken
            val refreshToken = authTokens.refreshToken
            Constants.refreshToken = refreshToken
        }
        binding?.buttonLogin?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val email = binding?.etEmail?.text.toString()
                val password = binding?.etPassword?.text.toString()
                val encodedPassword = Base64.encodeToBase64(password)
                if (ValidPatterns.isValidEmail(email) && ValidPatterns.isValidPassword(password)) {
                    // Navigate to the next screen if both email and password are valid
                    viewModel.login(email,encodedPassword)

                    binding?.progressBar?.visibility = View.VISIBLE

                }else{
                    binding?.progressBar?.visibility = View.GONE

                    val message = getString(R.string.required_fields_are_empty)
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)

                }
            } else {
                binding?.progressBar?.visibility = View.GONE

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)            }
        }
        binding?.signup?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.progressBar?.visibility = View.GONE
                findNavController().navigate(R.id.navigate_from_login_to_register)
            } else {
                binding?.progressBar?.visibility = View.GONE

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        viewModel.loginResult.observe(viewLifecycleOwner) { status ->
           if (status =="200") {
                findNavController().navigate(R.id.navigate_from_login_to_todoMain)
            } else {

                   val message = "Invalid email or password."
                   DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
               binding?.progressBar?.visibility = View.GONE
           }
        }
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
