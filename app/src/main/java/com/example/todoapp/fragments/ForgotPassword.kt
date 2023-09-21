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
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.databinding.FragmentForgotPasswordBinding
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ForgotPassword : Fragment() {
    private var binding:FragmentForgotPasswordBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password_title)
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_forgot_password_to_login)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_forgot_password_to_login)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setupTextChangeListeners()
        binding?.btnSubmit?.visibility = View.INVISIBLE
        binding?.btnSubmit1?.visibility = View.VISIBLE

        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val viewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(UserViewModel::class.java)
        binding?.btnSubmit?.setOnClickListener{
            val email =binding?.enterEmail?.text.toString()
            Constants.userEmail = email
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                    viewModel?.forgotPasswordRequestOtp(email)
                binding?.progressBar?.visibility = View.VISIBLE
            }    else {
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_forgotPassword_to_forgotPasswordOtp)
                viewModel.msg.observe(viewLifecycleOwner){msg ->
                    val message = msg.toString()
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }
            }else{
                binding?.progressBar?.visibility = View.INVISIBLE
                val message = status.toString()
                Snackbar.make(requireView(),message, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel?.otpResult?.observe(viewLifecycleOwner){ otp ->
            val newOtp = otp.toString()
            Constants.userOtp = newOtp
        }
    }
    private fun setupTextChangeListeners() {
        binding?.enterEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    binding?.enterEmail?.error = getString(R.string.invalid_or_empty_email_id)
                    binding?.btnSubmit?.visibility = View.INVISIBLE
                    binding?.btnSubmit1?.visibility = View.VISIBLE
                } else {
                    binding?.enterEmail?.error = null // Clear error message
                    binding?.btnSubmit?.visibility = View.VISIBLE
                    binding?.btnSubmit1?.visibility = View.INVISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          binding = FragmentForgotPasswordBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}

